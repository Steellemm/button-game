package org.stlm.game.button.buttongame.model.event

import org.stlm.game.button.buttongame.model.FrontButton

data class NewBossStateEvent(
    val hp: Int,
    val hpChange: Int,
    val text: String,
    val buttons: List<FrontButton>? = null,
): OutputMessage
