package org.stlm.game.button.buttongame.service.level

import org.stlm.game.button.buttongame.model.*
import org.stlm.game.button.buttongame.model.event.*
import org.stlm.game.button.buttongame.utils.calculateLeftTime

class GuessBossLevel(
    private val playersNames: Map<String, Player>,
    private val gameState: GameState
): GameLevelCreator {

    private var finished = false
    private val clickedPlayers: MutableSet<String> = mutableSetOf()

    private lateinit var buttons: List<FrontButton>
    private var hp: Int = 0
    private val poll: MutableMap<Int, Int> = mutableMapOf()

    override fun generateLevel() {
        newPoll()
        finished = false
        hp = (playersNames.size * 2) - 1 + gameState.level
    }

    private fun newPoll() {
        clickedPlayers.clear()
        buttons = generateButtons(gameState.level, gameState.buttonCount)
        buttons.forEach { poll[it.id] = 0 }
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
                hint = "Выберите и нажмите одну кнопку. Больше одинаковых нажатий - больше урон",
                boss = ButtonBoss(
                    name = "Gus",
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
        poll.computeIfPresent(buttonId) { _, count ->
            count + 1
        }
        if (clickedPlayers.size >= playersNames.size) {
            val damage = poll.values.max()
            hp -= damage
            if (hp <= 0) {
                finished = true
                return EMPTY_CLIENT_MESSAGES
            } else {
                newPoll()
                return ClientMessages(
                    lobbyMessage = NewBossStateEvent(
                        hp = hp,
                        hpChange = damage,
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