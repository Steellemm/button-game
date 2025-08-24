package org.stlm.game.button.buttongame.utils

import org.stlm.game.button.buttongame.model.FrontButton

/**
 * Цветные эмодзи
 */
fun level1(buttonsCount: Int): List<FrontButton> {
    return level(
        buttonsCount = buttonsCount,
        color = true,
        emoji = true,
        emojiRepeat = false,
        symbol = false,
        symbolRepeat = false,
    )
}

/**
 * Одноцветные эмодзи
 */
fun level2(buttonsCount: Int): List<FrontButton> {
    return level(
        buttonsCount = buttonsCount,
        color = false,
        emoji = true,
        emojiRepeat = false,
        symbol = false,
        symbolRepeat = false,
    )
}

/**
 * Цветные, эмодзи (повторяются) + символ
 */
fun level3(buttonsCount: Int): List<FrontButton> {
    return level(
        buttonsCount = buttonsCount,
        color = true,
        emoji = true,
        emojiRepeat = true,
        symbol = true,
        symbolRepeat = false,
    )
}

/**
 * Одноцветные, эмодзи (повторяются) + символ
 */
fun level4(buttonsCount: Int): List<FrontButton> {
    return level(
        buttonsCount = buttonsCount,
        color = false,
        emoji = true,
        emojiRepeat = true,
        symbol = true,
        symbolRepeat = false,
    )
}

/**
 * Разноцветные символы
 */
fun level5(buttonsCount: Int): List<FrontButton> {
    return level(
        buttonsCount = buttonsCount,
        color = true,
        emoji = false,
        emojiRepeat = false,
        symbol = true,
        symbolRepeat = false,
    )
}

/**
 * Одноцветные символы
 */
fun level6(buttonsCount: Int): List<FrontButton> {
    return level(
        buttonsCount = buttonsCount,
        color = false,
        emoji = false,
        emojiRepeat = false,
        symbol = true,
        symbolRepeat = false,
    )
}
/**
 * Одноцветные символы с повторением
 */
fun lastLevel(buttonsCount: Int): List<FrontButton> {
    return level(
        buttonsCount = buttonsCount,
        color = false,
        emoji = false,
        emojiRepeat = false,
        symbol = true,
        symbolRepeat = false,
    )
}

fun level(
    buttonsCount: Int,
    color: Boolean,
    emoji: Boolean,
    emojiRepeat: Boolean,
    symbol: Boolean,
    symbolRepeat: Boolean
): List<FrontButton> {
    val chineseArray1 = generateUniqueArray(buttonsCount, CHINESE.size - 1)
    val shortChineseArray = generateUniqueArray(4, CHINESE.size - 1)
    val chineseArray2 = generateUniqueArray(buttonsCount, CHINESE.size - 1)
    val backgroundColor = backgroundColorList.random()
    val emojiArray = generateUniqueArray(buttonsCount, EMOJI.size - 1)
    val shortEmojiArray = generateUniqueArray(3, EMOJI.size - 1)
    return generateSequence(0) { it + 1 }
        .takeWhile { it < buttonsCount }
        .map {
            val symbol1: String = if (emoji && emojiRepeat) {
                EMOJI[shortEmojiArray.random()]
            } else if (emoji) {
                EMOJI[emojiArray[it]]
            } else if (symbolRepeat){
                CHINESE[shortChineseArray.random()]
            } else {
                CHINESE[chineseArray1[it]]
            }
            val symbol2: String = if (symbol) {
                CHINESE[chineseArray2[it]]
            } else {
                ""
            }
            FrontButton(
                id = it,
                text = symbol1 + symbol2,
                backgroundColor = if (color) backgroundColorList.random() else backgroundColor,
                backgroundTone = backgroundToneList.random(),
                height = heightList.random(),
                width = weightList.random(),
            )
        }.toList()
}