package org.stlm.game.button.buttongame.model.event

data class FoulEvent(
    val leftTime: Int,
    val changeTime: Int,
    val buttonId: Int,
): OutputMessage