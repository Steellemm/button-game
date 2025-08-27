package org.stlm.game.button.buttongame.model.event

import org.stlm.game.button.buttongame.model.Player

data class PlayerPassEvent(
    val player: Player
): OutputMessage
