package org.stlm.game.button.buttongame.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.stlm.game.button.buttongame.config.security.CustomHandshakeInterceptor
import org.stlm.game.button.buttongame.config.security.SetPrincipalHandler
import org.stlm.game.button.buttongame.service.GameService

@Configuration
@EnableWebSocketMessageBroker
class SocketBrokerConfig(
    val setPrincipalHandler: SetPrincipalHandler,
    val customHandshakeInterceptor: CustomHandshakeInterceptor,
) : WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.enableSimpleBroker("/lobby")
        config.setApplicationDestinationPrefixes("/app")
        config.setUserDestinationPrefix("/players")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry
            .addEndpoint("/button")
            .setHandshakeHandler(setPrincipalHandler)
            .addInterceptors(customHandshakeInterceptor)
            .setAllowedOriginPatterns("*")
            .withSockJS()
    }
}