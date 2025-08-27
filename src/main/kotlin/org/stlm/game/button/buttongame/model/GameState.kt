package org.stlm.game.button.buttongame.model

data class GameState (
    var gameStarted: Boolean = false,
    var gameStartTime: Long = 0L,
    var round: Int = 0,
    var bonusTime: Int = 0,
    var level: Int = 1,
    var buttonCount: Int = 3,
)