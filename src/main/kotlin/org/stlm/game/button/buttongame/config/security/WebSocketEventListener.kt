package org.stlm.game.button.buttongame.config.security

import mu.KLogging
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import org.springframework.web.socket.messaging.SessionSubscribeEvent
import org.stlm.game.button.buttongame.service.GameService


@Component
class WebSocketEventListener(
    val gameService: GameService
) {

    @EventListener
    fun afterConnectionClosed(event: SessionDisconnectEvent) {
        val principal = event.user as StompPrincipal
        gameService.removePlayer(principal.name)
    }

    @EventListener
    fun handleSessionSubscribeEvent(event: SessionSubscribeEvent) {
        try {
            val destination = event.message.headers.get("simpDestination", String::class.java)
            if (destination == "/lobby") {
                val principal = event.user as StompPrincipal
                gameService.addPlayer(principal.name)
            }
        } catch (e: Exception) {
            logger.error(e) { "handleSessionSubscribeEvent error" }
        }

    }

    private companion object : KLogging()

}