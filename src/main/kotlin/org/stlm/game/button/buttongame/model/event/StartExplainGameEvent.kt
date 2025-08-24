package org.stlm.game.button.buttongame.model.event

import org.stlm.game.button.buttongame.model.FrontButton

data class StartExplainGameEvent(
    val chooser: Boolean = false,
    val leftTime: Int,
    val round: Int,
    val button: FrontButton
): OutputMessage