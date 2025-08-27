package org.stlm.game.button.buttongame.service.level

import org.stlm.game.button.buttongame.model.*
import org.stlm.game.button.buttongame.model.event.*
import org.stlm.game.button.buttongame.utils.*

class SimpleLevel(
    private val playersNames: Map<String, Player>,
    private val gameState: GameState
) : GameLevelCreator {

    private var finished = false
    private val roundWinners: MutableSet<String> = mutableSetOf()
    private val explainBefore: MutableSet<String> = mutableSetOf()

    private var winButtonId: Int = -1
    private lateinit var explainer: String
    private lateinit var buttons: List<FrontButton>

    override fun clear() {
        finished = false
        explainBefore.clear()
    }

    override fun generateLevel() {
        buttons = generateButtons(gameState.level, gameState.buttonCount)
        winButtonId = buttons.random().id
        val candidates = playersNames.keys.toList().toSet() - explainBefore
        explainer = if (candidates.isNotEmpty()) {
            candidates.random()
        } else {
            explainBefore.clear()
            playersNames.keys.random()
        }
        explainBefore.add(explainer)
        finished = false
        roundWinners.clear()
    }

    override fun getInitialMessages(): ClientMessages {
        val leftTime = calculateLeftTime(gameState)
        val explainerShortId = playersNames[explainer]!!.id
        val ee = NewLevelGameEvent(
            leftTime = leftTime,
            buttons = listOf(buttons[winButtonId]),
            round = gameState.round,
            chooser = false,
            explainerId = explainerShortId,
            level = gameState.level,
            hint = "Explain your button to other players"
        )
        val ce = NewLevelGameEvent(
            leftTime = leftTime,
            buttons = buttons,
            round = gameState.round,
            chooser = true,
            explainerId = explainerShortId,
            level = gameState.level,
            hint = "Press the button that is now being explained"
        )
        val messages = playersNames.keys.map {
            if (it == explainer) {
                PlayerMessages(it, ee)
            } else {
                PlayerMessages(it, ce)
            }
        }
        return ClientMessages(
            playerMessages = messages,
        )
    }

    override fun click(playerId: String, buttonId: Int): ClientMessages {
        if (playerId == explainer || roundWinners.contains(playerId)) {
            return EMPTY_CLIENT_MESSAGES
        }
        if (buttonId != winButtonId) {
            gameState.bonusTime -= FOUL_TIME
            return ClientMessages(
                lobbyMessage = FoulEvent(
                    leftTime = calculateLeftTime(gameState),
                    changeTime = FOUL_TIME,
                    buttonId = buttonId
                ),
                playerMessages = listOf(PlayerMessages(playerId, WrongClickEvent(buttonId)))
            )
        }
        roundWinners.add(playerId)
        if (roundWinners.size >= playersNames.size - 1) {
            finished = true
            gameState.bonusTime += BONUS_TIME
            return ClientMessages(
                lobbyMessage = WinRoundEvent(leftTime = calculateLeftTime(gameState), changeTime = BONUS_TIME)
            )
        } else {
            return ClientMessages(
                lobbyMessage = PlayerPassEvent(playersNames[playerId]!!),
                playerMessages = listOf(PlayerMessages(playerId, RightClickEvent(buttonId)))
            )
        }

    }

    override fun type() = LevelType.REGULAR

    override fun isDone(): Boolean = finished
}