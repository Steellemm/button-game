package org.stlm.game.button.buttongame.service.level

import org.stlm.game.button.buttongame.model.*
import org.stlm.game.button.buttongame.model.event.ButtonBoss
import org.stlm.game.button.buttongame.model.event.NewBossStateEvent
import org.stlm.game.button.buttongame.model.event.NewLevelGameEvent
import org.stlm.game.button.buttongame.model.event.RightClickEvent
import org.stlm.game.button.buttongame.utils.calculateLeftTime

class EasyBossLevel(
    private val playersNames: Map<String, Player>,
    private val gameState: GameState
): GameLevelCreator {

    private var finished = false
    private val roundWinners: MutableSet<String> = mutableSetOf()
    private val clickedPlayers: MutableSet<String> = mutableSetOf()

    private lateinit var winButton: FrontButton
    private lateinit var buttons: List<FrontButton>
    private var fightRound = 0
    private var hp: Int = 0

    override fun generateLevel() {
        buttons = generateButtons(gameState.level, gameState.buttonCount)
        winButton = buttons.first()
        finished = false
        roundWinners.clear()
        hp = playersNames.size + gameState.level
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
                hint = "Press the button that Easer shows",
                boss = ButtonBoss(
                    name = "Easer",
                    text = winButton.text,
                    maxHp = hp
                ),
                background = 1
            )
        )
    }

    override fun click(playerId: String, buttonId: Int): ClientMessages {
        if (clickedPlayers.contains(playerId)) {
            return EMPTY_CLIENT_MESSAGES
        }
        clickedPlayers.add(playerId)
        if (buttonId == winButton.id) {
            roundWinners.add(playerId)
        }
        if (clickedPlayers.size == playersNames.size) {
            hp -= roundWinners.size
            if (hp <= 0) {
                finished = true
                return EMPTY_CLIENT_MESSAGES
            } else {
                takeNewButton()
                clickedPlayers.clear()
                roundWinners.clear()
                return ClientMessages(
                    lobbyMessage = NewBossStateEvent(
                        hp = hp,
                        hpChange = roundWinners.size,
                        text = winButton.text
                    )
                )
            }
        }
        return ClientMessages(
            playerMessages = listOf(PlayerMessages(playerId, RightClickEvent(buttonId))),
        )
    }


    override fun isDone(): Boolean = finished

    override fun clear() {
        clickedPlayers.clear()
        roundWinners.clear()
    }

    override fun type() = LevelType.BOSS

    private fun takeNewButton() {
        fightRound = (fightRound + 1) % buttons.size
        winButton = buttons[fightRound]
    }
}