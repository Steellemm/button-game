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
    private var startRoundTime: Long = -1
    private lateinit var explainer: String
    private lateinit var buttons: List<FrontButton>

    override fun clear() {
        finished = false
        explainBefore.clear()
        startRoundTime = -1
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
        startRoundTime = System.currentTimeMillis()
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
            hint = "Explain it!",
            buttonsNumber = gameState.buttonCount,
        )
        val ce = NewLevelGameEvent(
            leftTime = leftTime,
            buttons = buttons,
            round = gameState.round,
            chooser = true,
            explainerId = explainerShortId,
            level = gameState.level,
            hint = "Press the right button",
            buttonsNumber = gameState.buttonCount,
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
            val time = System.currentTimeMillis() - startRoundTime
            val status = when {
                time < 1500 -> RoundResult.EXCELLENT
                time < 2000 -> RoundResult.GREAT
                time < 6000 -> RoundResult.GOOD
                else -> RoundResult.TERRIBLE
            }
            return ClientMessages(
                lobbyMessage = WinRoundEvent(
                    leftTime = calculateLeftTime(gameState),
                    changeTime = BONUS_TIME,
                    status = status,
                )
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