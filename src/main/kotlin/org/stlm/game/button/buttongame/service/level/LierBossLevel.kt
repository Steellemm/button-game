package org.stlm.game.button.buttongame.service.level

import org.stlm.game.button.buttongame.model.*
import org.stlm.game.button.buttongame.model.event.*
import org.stlm.game.button.buttongame.utils.calculateLeftTime

class LierBossLevel(
    private val playersNames: Map<String, Player>,
    private val gameState: GameState
) : GameLevelCreator {

    private var finished = false
    private val roundWinners: MutableSet<String> = mutableSetOf()
    private val clickedPlayers: MutableSet<String> = mutableSetOf()

    private lateinit var winButton: FrontButton
    private lateinit var fakeButton: FrontButton
    private lateinit var buttons: List<FrontButton>
    private var isFakeButtonPressed = false
    private var hp: Int = 0

    override fun generateLevel() {
        newFight()
        finished = false
        hp = (playersNames.size * 2) - 3 + gameState.level
    }

    private fun newFight() {
        clickedPlayers.clear()
        roundWinners.clear()
        isFakeButtonPressed = false
        buttons = generateButtons(gameState.level, gameState.buttonCount)
        winButton = buttons.first()
        fakeButton = buttons.last()
    }

    override fun getInitialMessages(): ClientMessages {
        val leftTime = calculateLeftTime(gameState)
        val fakePlayer = playersNames.keys.random()
        return ClientMessages(
            playerMessages = playersNames.keys.map {
                PlayerMessages(
                    it,
                    NewLevelGameEvent(
                        leftTime = leftTime,
                        buttons = buttons,
                        round = gameState.round,
                        chooser = true,
                        explainerId = null,
                        level = gameState.level,
                        hint = "Кому-то из вас Lier показывает неверную кнопку. Не нажмите её",
                        boss = ButtonBoss(
                            name = "Lier",
                            text = if (it == fakePlayer) fakeButton.text else winButton.text,
                            maxHp = hp
                        ),
                        background = 1
                    )
                )
            }
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
        if (buttonId == fakeButton.id) {
            isFakeButtonPressed = true
        }
        if (clickedPlayers.size >= playersNames.size) {
            val damage = if (isFakeButtonPressed) 0 else roundWinners.size
            hp -= damage
            if (hp <= 0) {
                finished = true
                return EMPTY_CLIENT_MESSAGES
            } else {
                newFight()
                val fakePlayer = playersNames.keys.random()
                return ClientMessages(
                    playerMessages = playersNames.keys.map {
                        PlayerMessages(
                            it,
                            NewBossStateEvent(
                                hp = hp,
                                hpChange = damage,
                                text = if (it == fakePlayer) fakeButton.text else winButton.text,
                                buttons = buttons,
                            )
                        )
                    }
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