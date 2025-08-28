package org.stlm.game.button.buttongame.service.level

import org.stlm.game.button.buttongame.model.*
import org.stlm.game.button.buttongame.model.event.NewLevelGameEvent
import org.stlm.game.button.buttongame.model.event.RightClickEvent
import org.stlm.game.button.buttongame.model.event.WinRoundEvent
import org.stlm.game.button.buttongame.utils.*
import kotlin.math.min

class BonusLevel(
    private val playersNames: Map<String, Player>,
    private val gameState: GameState
) : GameLevelCreator {

    private val levelMap = listOf(
        2,
        2,
        4,
        4,
        5
    )

    private var finished = false

    private val poll: MutableMap<Int, Int> = mutableMapOf()
    private val bonus: MutableMap<Int, ChangeState> = mutableMapOf()
    private val clickedPlayers: MutableSet<String> = mutableSetOf()


    override fun generateLevel() {
        finished = false
        bonus.clear()
        poll.clear()
        clickedPlayers.clear()
        val buttonCountBonus = -min(levelMap.getOrElse(gameState.level - 1) { 5 }, gameState.buttonCount - 4)
        bonus[0] = ChangeState(
            bonusTime = gameState.level * 2 + playersNames.size
        )
        bonus[1] = ChangeState(
            changeButtonCount = buttonCountBonus
        )
        poll[0] = 0
        poll[1] = 0
    }

    override fun getInitialMessages(): ClientMessages = ClientMessages(
        lobbyMessage = NewLevelGameEvent(
            leftTime = calculateLeftTime(gameState),
            buttons = bonus.map {
                FrontButton(
                    id = it.key,
                    text = generateMessage(it.value),
                    backgroundColor = 5,
                    backgroundTone = 100,
                    height = 100,
                    width = 200,
                )
            },
            round = gameState.round,
            chooser = true,
            explainerId = null,
            level = gameState.level,
            hint = "Choose bonus for team. It is poll",
            boss = null,
            background = 2
        )
    )

    override fun click(playerId: String, buttonId: Int): ClientMessages {
        if (clickedPlayers.contains(playerId)) {
            return EMPTY_CLIENT_MESSAGES
        }
        clickedPlayers.add(playerId)
        poll.computeIfPresent(buttonId) { _, count ->
            count + 1
        }
        if (clickedPlayers.size >= playersNames.size) {
            val winButton = poll.maxBy { it.value }.key
            val change = bonus[winButton]!!
            gameState.bonusTime += change.bonusTime
            gameState.buttonCount += change.changeButtonCount
            finished = true
            return ClientMessages(
                lobbyMessage = WinRoundEvent(leftTime = calculateLeftTime(gameState), changeTime = change.bonusTime)
            )
        }
        return ClientMessages(
            playerMessages = listOf(PlayerMessages(playerId, RightClickEvent(buttonId))),
        )
    }

    override fun isDone(): Boolean = finished

    override fun clear() {
    }

    override fun type() = LevelType.BONUS

}
