package org.stlm.game.button.buttongame.service

import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import org.stlm.game.button.buttongame.model.ClientMessages
import org.stlm.game.button.buttongame.model.event.OutputMessage

@Service
class NotificationService(
    val simpMessagingTemplate: SimpMessagingTemplate
) {

    fun send(clientMessages: ClientMessages?) {
        clientMessages?.lobbyMessage?.let {
            sendToLobby(it)
        }
        clientMessages?.playerMessages?.forEach {
            sendToUser(it.id, it.message)
        }
    }

    fun sendToLobby(message: OutputMessage) {
        simpMessagingTemplate.convertAndSend("/lobby", message)
    }

    fun sendToUser(playerId: String, message: OutputMessage) {
        simpMessagingTemplate.convertAndSendToUser(playerId, "/lobby/player", message)
    }

}