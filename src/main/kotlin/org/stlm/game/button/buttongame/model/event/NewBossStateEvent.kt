package org.stlm.game.button.buttongame.model.event

data class NewBossStateEvent(
    val hp: Int,
    val hpChange: Int,
    val text: String
): OutputMessage
