package org.stlm.game.button.buttongame.service

import mu.KLogging
import org.springframework.stereotype.Service
import org.stlm.game.button.buttongame.model.GameState
import org.stlm.game.button.buttongame.model.LevelType
import org.stlm.game.button.buttongame.model.Player
import org.stlm.game.button.buttongame.model.event.*
import org.stlm.game.button.buttongame.service.level.BonusLevel
import org.stlm.game.button.buttongame.service.level.EasyBossLevel
import org.stlm.game.button.buttongame.service.level.GameLevelCreator
import org.stlm.game.button.buttongame.service.level.SimpleLevel
import org.stlm.game.button.buttongame.utils.*
import java.util.concurrent.ConcurrentHashMap

@Service
class GameService(
    val notificationService: NotificationService
) {

    val players = ConcurrentHashMap<String, Boolean>()
    private final val playersNames = ConcurrentHashMap<String, Player>()
    private final val gameState = GameState()
    val gameTimer = GameTimer()

    final var simpleLevel = SimpleLevel(playersNames, gameState)
    final var bossLevel = EasyBossLevel(playersNames, gameState)
    final var bonusLevel = BonusLevel(playersNames, gameState)

    var currentGameLevelCreator: GameLevelCreator? = null

    @Synchronized
    fun setReady(id: String, ready: Boolean) {
        if (players.containsKey(id) && !gameState.gameStarted) {
            players[id] = ready
            notificationService.sendToUser(id, ReadyEvent(players.size, ready))
            if (players.size > 1 && !players.values.contains(false)) {
                newRound()
                gameTimer.start(gameState) {
                    gameOver()
                }
            }
        }
    }

    @Synchronized
    fun clickButton(id: String, buttonId: Int) {
        if (!gameState.gameStarted || currentGameLevelCreator == null) {
            return
        }
        notificationService.send(currentGameLevelCreator?.click(id, buttonId))
        if (currentGameLevelCreator!!.isDone()) {
            newRound()
        }
    }

    @Synchronized
    private fun newRound() {
        if (!gameState.gameStarted) {
            gameState.gameStartTime = System.currentTimeMillis()
        }
        val newLevel = calculateNewLevel(gameState)
        if (currentGameLevelCreator?.type() == LevelType.BOSS) {
            currentGameLevelCreator = bonusLevel
        } else if (currentGameLevelCreator?.type() == LevelType.BONUS) {
            currentGameLevelCreator = simpleLevel
            gameState.round++
            gameState.buttonCount++
            gameState.level = newLevel
        } else if (newLevel != gameState.level) {
            currentGameLevelCreator = bossLevel
            gameState.round++
            gameState.buttonCount++
        } else {
            currentGameLevelCreator = simpleLevel
            gameState.round++
            gameState.buttonCount++
        }
        gameState.gameStarted = true
        currentGameLevelCreator!!.generateLevel()
        notificationService.send(currentGameLevelCreator!!.getInitialMessages())
        gameState.gameStarted = true
    }

    @Synchronized
    fun gameOver() {
        if (gameState.gameStarted) {
            notificationService.sendToLobby(GameOverEvent(gameState.round))
            stopGame()
        }
    }

    @Synchronized
    fun addPlayer(id: String, name: String) {
        logger.info { "Adding player $id with name $name" }
        if (gameState.gameStarted) {
            return
        }
        players[id] = false
        playersNames[id] = Player(playersNames.size, name)
        notificationService.sendToLobby(PlayersNumberChangeEvent(players.size, playersNames.values.toList()))
    }

    @Synchronized
    fun removePlayer(id: String) {
        logger.info { "Remove player $id" }
        players.remove(id)
        playersNames.remove(id)
        notificationService.sendToLobby(PlayersNumberChangeEvent(players.size, playersNames.values.toList()))
        if (players.isEmpty()) {
            stopGame()
        }
    }

    private fun stopGame() {
        gameState.gameStarted = false
        gameState.round = 0
        gameState.gameStartTime = 0L
        gameState.bonusTime = 0
        simpleLevel.clear()
        players.replaceAll { _, _ ->  false}
    }

    private companion object : KLogging()
}