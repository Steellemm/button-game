package org.stlm.game.button.buttongame.utils

import org.stlm.game.button.buttongame.model.FrontButton
import org.stlm.game.button.buttongame.model.GameState
import kotlin.random.Random

const val INITIAL_TIME = 90
const val BONUS_TIME = 10
const val FOUL_TIME = 5


val backgroundColorList = listOf(0, 2, 3)
val backgroundToneList = listOf(40, 50, 60, 70, 80, 90, 100)
val heightList = listOf(60, 80, 100, 100, 100, 120, 140)
val weightList = listOf(80, 100, 100, 100, 120, 140, 160)

fun generateButtons(round: Int): List<FrontButton> {
    val buttonsCount = round + 3
    return when {
        round < 3 -> level1(buttonsCount)
        round < 6 -> level2(buttonsCount)
        round < 11 -> level3(buttonsCount)
        round < 18 -> level4(buttonsCount)
        round < 22 -> level5(buttonsCount)
        round < 27 -> level6(buttonsCount)
        else -> lastLevel(buttonsCount)
    }
}

fun calculateLeftTime(gameState: GameState): Int {
    if (gameState.gameStartTime == 0L){
        return INITIAL_TIME
    }
    return (INITIAL_TIME - (System.currentTimeMillis() - gameState.gameStartTime) / 1000 + gameState.bonusTime).toInt()
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