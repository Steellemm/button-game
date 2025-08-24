package org.stlm.game.button.buttongame.model.event

import org.stlm.game.button.buttongame.model.FrontButton

data class StartChooserGameEvent(
    val chooser: Boolean = true,
    val leftTime: Int,
    val round: Int,
    val buttons: List<FrontButton> = emptyList()
): OutputMessage