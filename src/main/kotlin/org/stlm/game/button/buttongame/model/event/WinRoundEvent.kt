package org.stlm.game.button.buttongame.model.event

import org.stlm.game.button.buttongame.model.RoundResult

data class WinRoundEvent(
    val leftTime: Int,
    val changeTime: Int,
    val status: RoundResult = RoundResult.SKIP
): OutputMessage