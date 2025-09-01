package org.stlm.game.button.buttongame.model.event

import org.stlm.game.button.buttongame.model.FrontButton

data class NewLevelGameEvent(
    val chooser: Boolean = true,
    val leftTime: Int,
    val round: Int,
    val buttons: List<FrontButton> = emptyList(),
    val explainerId: Int?,
    val level: Int,
    val buttonsNumber: Int,
    val hint: String,
    val boss: ButtonBoss? = null,
    /**
     * 0 - default
     * 1 - red
     * 2 - yellow
     */
    val background: Int = 0
): OutputMessage

data class ButtonBoss(
    val text: String,
    val maxHp: Int,
    val name: String
)