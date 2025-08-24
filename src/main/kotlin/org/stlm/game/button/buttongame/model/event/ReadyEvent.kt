package org.stlm.game.button.buttongame.model.event

data class ReadyEvent(
    val playersCount: Int,
    val ready: Boolean,
): OutputMessage