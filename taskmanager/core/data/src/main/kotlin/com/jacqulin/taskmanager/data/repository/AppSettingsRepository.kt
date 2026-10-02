package com.jacqulin.taskmanager.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jacqulin.taskmanager.core.model.ColorPalette
import com.jacqulin.taskmanager.core.model.DarkThemeConfig
import com.jacqulin.taskmanager.data.domain.AppSettingsRepository
import com.jacqulin.taskmanager.data.model.AppSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "app_settings")

@Singleton
class AppSettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : AppSettingsRepository {

    private object PreferencesKeys {
        val DARK_THEME_CONFIG = stringPreferencesKey("dark_theme_config")
        val COLOR_PALETTE = stringPreferencesKey("color_palette")
    }

    override val appSettings: Flow<AppSettings> = context.dataStore.data.map { preferences ->
        AppSettings(
            darkThemeConfig = preferences[PreferencesKeys.DARK_THEME_CONFIG]?.let {
                runCatching { DarkThemeConfig.valueOf(it) }.getOrDefault(DarkThemeConfig.FOLLOW_SYSTEM)
            } ?: DarkThemeConfig.FOLLOW_SYSTEM,

            colorPalette = preferences[PreferencesKeys.COLOR_PALETTE]?.let {
                runCatching { ColorPalette.valueOf(it) }.getOrDefault(ColorPalette.CYAN)
            } ?: ColorPalette.CYAN
        )
    }

    override suspend fun setColorPalette(colorPalette: ColorPalette) {
        context.dataStore.edit { it[PreferencesKeys.COLOR_PALETTE] = colorPalette.name }
    }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        context.dataStore.edit { it[PreferencesKeys.DARK_THEME_CONFIG] = darkThemeConfig.name }
    }
}
