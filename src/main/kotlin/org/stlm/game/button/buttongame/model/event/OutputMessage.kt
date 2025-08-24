package org.stlm.game.button.buttongame.model.event

interface OutputMessage {

    val type: String
        get() = this::class.java.simpleName

}