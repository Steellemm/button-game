package org.stlm.game.button.buttongame.utils

import org.stlm.game.button.buttongame.model.ChangeState
import org.stlm.game.button.buttongame.model.GameState
import kotlin.random.Random
import kotlin.math.max

const val INITIAL_TIME = 90
const val BONUS_TIME = 10
const val FOUL_TIME = 5


val backgroundColorList = listOf(0, 2, 3)
val backgroundToneList = listOf(40, 50, 60, 70, 80, 90, 100)
val heightList = listOf(60, 80, 100, 100, 100, 120, 140)
val weightList = listOf(80, 100, 100, 100, 120, 140, 160)

fun calculateNewLevel(gameState: GameState): Int {
    return when {
        gameState.round <= 2 -> 1
        gameState.round <= 4 -> 2
        gameState.round <= 15 -> 3
        gameState.round <= 20 -> 4
        gameState.round <= 25 -> 5
        gameState.round <= 30 -> 6
        else -> 7
    }
}

fun calculateLeftTime(gameState: GameState): Int {
    if (gameState.gameStartTime == 0L){
        return INITIAL_TIME
    }
    return max(0, (INITIAL_TIME - (System.currentTimeMillis() - gameState.gameStartTime) / 1000 + gameState.bonusTime).toInt())
}

fun generateMessage(changeState: ChangeState): String {
    val part1 = if (changeState.bonusTime > 0) {
        "+${changeState.bonusTime}s"
    } else {
        null
    }
    val part2 = if (changeState.changeButtonCount < 0) {
        "${changeState.changeButtonCount} btns"
    } else {
        null
    }
    return sequenceOf(part1, part2).filterNotNull().joinToString("\n")
}

fun generateUniqueArray(length: Int, maxSize: Int): IntArray {
    require(length >= 0) { "Length must be non-negative" }
    require(maxSize >= 0) { "Max size must be non-negative" }
    require(length <= maxSize + 1) {
        "Length cannot exceed maxSize + 1 (${maxSize + 1}) for unique values"
    }

    if (length == 0) return intArrayOf()
    if (length == maxSize + 1) return IntArray(length) { it }

    // Create a list of all possible numbers
    val allNumbers = (0..maxSize).toMutableList()
    val result = IntArray(length)

    for (i in 0 until length) {
        val randomIndex = Random.nextInt(i, allNumbers.size)
        // Swap elements
        val temp = allNumbers[i]
        allNumbers[i] = allNumbers[randomIndex]
        allNumbers[randomIndex] = temp

        result[i] = allNumbers[i]
    }

    return result
}