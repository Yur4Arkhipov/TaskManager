package com.jacqulin.taskmanager.feature.settings.presentation

import androidx.compose.ui.graphics.Color

/**
 * Available color schemes for the app.
 */
enum class ColorScheme(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val key: String,
    val label: String
) {
    PURPLE(
        primary = Color(0xFF6750A4),
        secondary = Color(0xFF625B71),
        tertiary = Color(0xFF7D5260),
        key = "PURPLE",
        label = "Фиолетовая"
    ),
    BLUE(
        primary = Color(0xFF3D5AFE),
        secondary = Color(0xFF5F6368),
        tertiary = Color(0xFF497D4C),
        key = "BLUE",
        label = "Синяя"
    ),
    GREEN(
        primary = Color(0xFF006D3B),
        secondary = Color(0xFF5F6368),
        tertiary = Color(0xFFB55D40),
        key = "GREEN",
        label = "Зелёная"
    ),
    ORANGE(
        primary = Color(0xFFBA68C8),
        secondary = Color(0xFF7C4DFF),
        tertiary = Color(0xFFFFB74D),
        key = "ORANGE",
        label = "Оранжевая"
    ),
    TEAL(
        primary = Color(0xFF007983),
        secondary = Color(0xFF4D576E),
        tertiary = Color(0xFF7C4DFF),
        key = "TEAL",
        label = "Бирюзовая"
    );

    companion object {
        fun fromKey(key: String): ColorScheme {
            return entries.find { it.key == key } ?: PURPLE
        }
    }
}
