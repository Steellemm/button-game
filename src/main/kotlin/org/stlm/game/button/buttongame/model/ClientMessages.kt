package org.stlm.game.button.buttongame.model

import org.stlm.game.button.buttongame.model.event.OutputMessage

data class ClientMessages(
    val lobbyMessage: OutputMessage? = null,
    val playerMessages: List<PlayerMessages> = emptyList(),
)

data class PlayerMessages(
    val id: String,
    val message: OutputMessage,
)

val EMPTY_CLIENT_MESSAGES = ClientMessages(null, emptyList())