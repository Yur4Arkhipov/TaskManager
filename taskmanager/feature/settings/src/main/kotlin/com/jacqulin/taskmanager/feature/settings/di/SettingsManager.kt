package com.jacqulin.taskmanager.feature.settings.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jacqulin.taskmanager.feature.settings.presentation.ColorScheme
import com.jacqulin.taskmanager.feature.settings.presentation.ThemePreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * Singleton manager for app settings with DataStore persistence.
 * Provides flows for theme preference and color scheme that can be observed
 * by both the SettingsViewModel and MainActivity.
 */
@Singleton
class SettingsManager(
    context: Context
) {
    private val dataStore = context.dataStore
    private val scope = CoroutineScope(SupervisorJob())

    // Keys
    private val THEME_KEY = stringPreferencesKey("theme_preference")
    private val COLOR_SCHEME_KEY = stringPreferencesKey("color_scheme")

    // Current settings
    private val _themePreference = dataStore.data
        .map { preferences ->
            when (preferences[THEME_KEY]) {
                "DARK" -> ThemePreference.Dark
                "LIGHT" -> ThemePreference.Light
                else -> ThemePreference.System
            }
        }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ThemePreference.System
        )

    private val _colorScheme = dataStore.data
        .map { preferences ->
            ColorScheme.fromKey(preferences[COLOR_SCHEME_KEY] ?: "PURPLE")
        }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ColorScheme.PURPLE
        )

    val themePreference: StateFlow<ThemePreference> = _themePreference
    val colorScheme: StateFlow<ColorScheme> = _colorScheme

    /**
     * Update the theme preference and persist it.
     */
    suspend fun setThemePreference(theme: ThemePreference) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = when (theme) {
                ThemePreference.Dark -> "DARK"
                ThemePreference.Light -> "LIGHT"
                ThemePreference.System -> "SYSTEM"
            }
        }
    }

    /**
     * Update the color scheme and persist it.
     */
    suspend fun setColorScheme(colorScheme: ColorScheme) {
        dataStore.edit { preferences ->
            preferences[COLOR_SCHEME_KEY] = colorScheme.key
        }
    }

    /**
     * Reset all settings to defaults.
     */
    suspend fun resetSettings() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    /**
     * Get the raw theme preference string from DataStore.
     * Used by MainActivity to determine which theme to apply.
     */
    fun getThemePreferenceString(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: "SYSTEM"
        }
    }
}
