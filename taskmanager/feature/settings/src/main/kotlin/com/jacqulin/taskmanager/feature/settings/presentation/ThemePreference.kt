package com.jacqulin.taskmanager.feature.settings.presentation

/**
 * Theme preference options for the app.
 */
sealed interface ThemePreference {

    data object System : ThemePreference {
        override val label: String = "Системная"
    }

    data object Light : ThemePreference {
        override val label: String = "Светлая"
    }

    data object Dark : ThemePreference {
        override val label: String = "Тёмная"
    }

    val label: String

    companion object {
        val entries: List<ThemePreference> = listOf(System, Light, Dark)
    }
}
