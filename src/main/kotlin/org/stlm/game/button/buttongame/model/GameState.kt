package org.stlm.game.button.buttongame.model

data class GameState (
    var gameStarted: Boolean = false,
    var gameStartTime: Long = 0L,
    var round: Int = 0,
    var winButtonId: Int = 0,
    var explainer: String = "",
    var bonusTime: Int = 0,
    val roundWinners: MutableSet<String> = mutableSetOf(),
    val explainers: MutableSet<String> = mutableSetOf()
)