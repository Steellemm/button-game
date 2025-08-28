package org.stlm.game.button.buttongame.service

import org.springframework.stereotype.Component
import org.stlm.game.button.buttongame.model.GameState

@Component
class GameStateComponent {
    private val gameState: GameState = GameState()

    fun getGameState(id: String): GameState = gameState
}