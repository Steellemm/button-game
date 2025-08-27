package org.stlm.game.button.buttongame.model

data class FrontButton(
    /**
     * Id кнопки, нужно отправлять обратно на бэк
     */
    val id: Int,
    /**
     * Процентная ширина кнопки. Кнопки с одинаковым процентом должны иметь одинаковый размер
     */
    val width: Int = 100,
    /**
     * Процентная высота кнопки.
     */
    val height: Int = 100,
    /**
     * Текст, который отобразить на кнопке. Может содержать код https://www.w3schools.com/html/html_emojis.asp
     */
    val text: String,
    /**
     * Форма кнопки. 1 - это квадрат. Остальные добавим потом
     */
    val type: Int = 1,
    /**
     * Цвет кнопки.
     * 0 - белый
     * 1 - черный
     * 2 - зеленый
     * 3 - красный
     * 4 - синий
     * 5 - желтый
     */
    val backgroundColor: Int = 0,
    /**
     * Оттенок кнопки. От 0 до 10. 0 - самый насыщенный цвет, 100 - самый тусклый
     */
    val backgroundTone: Int = 0,
    /**
     * Цвет текста
     */
    val textColor: Int = 1,
)
