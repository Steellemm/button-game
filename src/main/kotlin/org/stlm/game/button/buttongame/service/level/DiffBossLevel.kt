package org.stlm.game.button.buttongame.service.level

import org.stlm.game.button.buttongame.model.*
import org.stlm.game.button.buttongame.model.event.*
import org.stlm.game.button.buttongame.utils.calculateLeftTime
import kotlin.math.max

class DiffBossLevel(
    private val playersNames: Map<String, Player>,
    private val gameState: GameState
): GameLevelCreator {

    private var finished = false
    private val clickedPlayers: MutableSet<String> = mutableSetOf()
    private val clickedButtons: MutableSet<Int> = mutableSetOf()

    private lateinit var buttons: List<FrontButton>
    private var maxHp: Int = 0
    private var hp: Int = 0

    override fun generateLevel() {
        newFight()
        maxHp = max(playersNames.size - 2 + gameState.level, 2)
        hp = maxHp
    }

    private fun newFight() {
        clickedPlayers.clear()
        clickedButtons.clear()
        buttons = generateButtons(gameState.level, gameState.buttonCount)
        finished = false
    }

    override fun getInitialMessages(): ClientMessages {
        val leftTime = calculateLeftTime(gameState)
        return ClientMessages(
            lobbyMessage = NewLevelGameEvent(
                leftTime = leftTime,
                buttons = buttons,
                round = gameState.round,
                chooser = true,
                explainerId = null,
                level = gameState.level,
                hint = "Нажмите разные кнопки. Если нажать одинаковую, то Diffy восстановит здоровье",
                boss = ButtonBoss(
                    name = "Diffy",
                    text = "",
                    maxHp = hp
                ),
                background = 1,
                buttonsNumber = gameState.buttonCount,
            )
        )
    }

    override fun click(playerId: String, buttonId: Int): ClientMessages {
        if (clickedPlayers.contains(playerId)) {
            return EMPTY_CLIENT_MESSAGES
        }
        clickedPlayers.add(playerId)
        if (clickedButtons.contains(buttonId)) {
            val hpChange = hp - maxHp
            hp = maxHp
            newFight()
            return ClientMessages(
                lobbyMessage = NewBossStateEvent(
                    hp = hp,
                    hpChange = hpChange,
                    text = "",
                    buttons = buttons,
                    status = RoundResult.TERRIBLE
                )
            )
        } else {
            clickedButtons.add(buttonId)
        }
        if (clickedPlayers.size >= playersNames.size || clickedPlayers.size >= buttons.size) {
            hp -= clickedPlayers.size
            if (hp <= 0) {
                finished = true
                return EMPTY_CLIENT_MESSAGES
            } else {
                newFight()
                return ClientMessages(
                    lobbyMessage = NewBossStateEvent(
                        hp = hp,
                        hpChange = clickedPlayers.size,
                        text = "",
                        buttons = buttons,
                    )
                )
            }
        }
        return ClientMessages(
            lobbyMessage = PlayerPassEvent(playersNames[playerId]!!),
            playerMessages = listOf(PlayerMessages(playerId, RightClickEvent(buttonId))),
        )
    }


    override fun isDone(): Boolean = finished

    override fun clear() {
    }

    override fun type() = LevelType.BOSS
}