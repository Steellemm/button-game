package org.stlm.game.button.buttongame.model.event

import org.stlm.game.button.buttongame.model.Player

data class PlayerNotReadyEvent(
    val player: Player
): OutputMessage
