package org.stlm.game.button.buttongame.model.event

import org.stlm.game.button.buttongame.model.FrontButton
import org.stlm.game.button.buttongame.model.RoundResult

data class NewBossStateEvent(
    val hp: Int,
    val hpChange: Int,
    val text: String,
    val buttons: List<FrontButton>? = null,
    val status: RoundResult = RoundResult.SKIP
): OutputMessage
