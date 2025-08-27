package org.stlm.game.button.buttongame.service.level

import org.stlm.game.button.buttongame.model.ClientMessages
import org.stlm.game.button.buttongame.model.FrontButton
import org.stlm.game.button.buttongame.model.LevelType
import org.stlm.game.button.buttongame.utils.*

interface GameLevelCreator {

    fun generateLevel()

    fun getInitialMessages(): ClientMessages

    fun click(playerId: String, buttonId: Int): ClientMessages

    fun isDone(): Boolean

    fun clear()

    fun type(): LevelType

    fun generateButtons(level: Int, count: Int): List<FrontButton> = when (level) {
        1 -> level1(count)
        2 -> level2(count)
        3 -> level3(count)
        4 -> level4(count)
        5 -> level5(count)
        6 -> level6(count)
        else -> lastLevel(count)
    }
}