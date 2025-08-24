package org.stlm.game.button.buttongame.controller

import mu.KLogging
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller
import org.stlm.game.button.buttongame.model.input.PressedButtonRequest
import org.stlm.game.button.buttongame.model.input.ReadyRequest
import org.stlm.game.button.buttongame.service.GameService
import java.security.Principal


@Controller
class GameController(
    val gameService: GameService
) {

    @MessageMapping("/lobby/input/ready")
    fun changeRotationDirection(@Payload message: ReadyRequest, principal: Principal) {
        try {
            gameService.setReady(principal.name, message.ready)
        } catch (e: Exception) {
            logger.error(e){"pressButton error"}
        }
    }

    @MessageMapping("/lobby/input/pressButton")
    fun changeRotationDirection(@Payload message: PressedButtonRequest, principal: Principal) {
        try {
            gameService.clickButton(principal.name, message.buttonId)
        } catch (e: Exception) {
            logger.error(e){"pressButton error"}
        }
    }

    private companion object : KLogging()
}