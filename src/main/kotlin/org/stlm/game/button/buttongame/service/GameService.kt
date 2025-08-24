package org.stlm.game.button.buttongame.service

import mu.KLogging
import org.springframework.stereotype.Service
import org.stlm.game.button.buttongame.model.GameState
import org.stlm.game.button.buttongame.model.event.*
import org.stlm.game.button.buttongame.utils.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

@Service
class GameService(
    val notificationService: NotificationService
) {

    val players = ConcurrentHashMap<String, Boolean>()
    val gameState = GameState()
    val gameTimer = GameTimer()

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
        if (!gameState.gameStarted || id == gameState.explainer || gameState.roundWinners.contains(id)) {
            return
        }
        if (gameState.winButtonId != buttonId && !gameState.roundWinners.contains(id)) {
            gameState.bonusTime -= FOUL_TIME
            notificationService.sendToUser(id, WrongClickEvent(buttonId))
            notificationService.sendToLobby(
                FoulEvent(
                    leftTime = calculateLeftTime(gameState),
                    changeTime = FOUL_TIME,
                    buttonId = buttonId
                )
            )
            return
        }
        if (gameState.winButtonId == buttonId && !gameState.roundWinners.contains(id)) {
            gameState.roundWinners.add(id)
            notificationService.sendToUser(id, RightClickEvent(buttonId))
            if (gameState.roundWinners.size == players.size - 1) {
                gameState.bonusTime += BONUS_TIME
                notificationService.sendToLobby(WinRoundEvent(leftTime = calculateLeftTime(gameState), changeTime = BONUS_TIME))
                newRound()
            }
        }
    }

    @Synchronized
    private fun newRound() {
        gameState.round++
        gameState.gameStarted = true
        gameState.roundWinners.clear()
        val buttons = generateButtons(gameState.round)
        gameState.winButtonId = Random.nextInt(buttons.size)
        val candidates: Set<String> = players.keys().toList().toSet() - gameState.explainers
        gameState.explainer = if (candidates.isEmpty()) {
            gameState.explainers.clear()
            players.keys().toList().random()
        } else {
            candidates.random()
        }
        gameState.explainers.add(gameState.explainer)
        val ee = StartExplainGameEvent(
            leftTime = calculateLeftTime(gameState),
            button = buttons[gameState.winButtonId],
            round = gameState.round,
        )
        val ce = StartChooserGameEvent(
            leftTime = calculateLeftTime(gameState),
            buttons = buttons,
            round = gameState.round,
        )
        players.keys().toList().forEach {
            if (it == gameState.explainer) {
                notificationService.sendToUser(it, ee)
            } else {
                notificationService.sendToUser(it, ce)
            }
        }
        if (gameState.round == 1) {
            gameState.gameStartTime = System.currentTimeMillis()
        }
    }

    @Synchronized
    fun gameOver() {
        if (gameState.gameStarted) {
            notificationService.sendToLobby(GameOverEvent(gameState.round))
            stopGame()
        }
    }

    @Synchronized
    fun addPlayer(id: String) {
        logger.info { "Adding player $id" }
        if (gameState.gameStarted) {
            return
        }
        players[id] = false
        notificationService.sendToLobby(PlayersNumberChangeEvent(players.size))
    }

    @Synchronized
    fun removePlayer(id: String) {
        logger.info { "Remove player $id" }
        players.remove(id)
        notificationService.sendToLobby(PlayersNumberChangeEvent(players.size))
        if (players.isEmpty()) {
            stopGame()
        }
    }

    private fun stopGame() {
        gameState.gameStarted = false
        gameState.round = 0
        gameState.gameStartTime = 0L
        players.replaceAll { _, _ ->  false}
    }

    private companion object : KLogging()
}