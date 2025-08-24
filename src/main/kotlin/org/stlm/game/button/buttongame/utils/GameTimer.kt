package org.stlm.game.button.buttongame.utils

import mu.KLogging
import org.stlm.game.button.buttongame.model.GameState
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit


class GameTimer {

    private var scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

    private var timerFuture: ScheduledFuture<*>? = null

    fun start(gameState: GameState, stopFun: (() -> Unit)) {
        timerFuture = scheduler.scheduleAtFixedRate({
            val leftTime = calculateLeftTime(gameState)
            if (leftTime < 0) {
                stopTimer()
                stopFun.invoke()
            }
        }, 0, 1, TimeUnit.SECONDS)
    }

    private fun stopTimer() {
        timerFuture?.cancel(true)
        timerFuture = null
    }

    private companion object : KLogging()
}