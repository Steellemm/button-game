package org.stlm.game.button.buttongame.config.security

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.support.DefaultHandshakeHandler
import org.springframework.web.util.WebUtils
import java.security.Principal
import java.util.*

@Component
class SetPrincipalHandler : DefaultHandshakeHandler() {

    override fun determineUser(
        request: ServerHttpRequest,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Principal {
        val servletRequest: HttpServletRequest = (request as ServletServerHttpRequest).servletRequest
        val playerName = WebUtils.getCookie(servletRequest, "button-game-name")
        return StompPrincipal(UUID.randomUUID().toString(), playerName?.value ?: "NoName")
    }
}