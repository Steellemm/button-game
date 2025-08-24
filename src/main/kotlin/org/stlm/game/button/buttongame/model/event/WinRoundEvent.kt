package org.stlm.game.button.buttongame.model.event

data class WinRoundEvent(
    val leftTime: Int,
    val changeTime: Int,
): OutputMessage