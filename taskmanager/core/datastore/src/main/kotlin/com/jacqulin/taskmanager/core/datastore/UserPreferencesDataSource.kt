package com.jacqulin.taskmanager.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jacqulin.taskmanager.core.datastore.model.UserPreferences
import com.jacqulin.taskmanager.core.model.ColorPalette
import com.jacqulin.taskmanager.core.model.DarkThemeConfig
import com.jacqulin.taskmanager.core.model.VoiceInputMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object PreferencesKeys {
        val DARK_THEME_CONFIG = stringPreferencesKey("dark_theme_config")
        val COLOR_PALETTE = stringPreferencesKey("color_palette")
        val VOICE_INPUT_MODE = stringPreferencesKey("voice_input_mode")
        val YANDEX_IAM_TOKEN = stringPreferencesKey("yandex_iam_token")
        val YANDEX_FOLDER_ID = stringPreferencesKey("yandex_folder_id")
    }

    val userPreferences: Flow<UserPreferences> = dataStore.data
        .map { preferences ->
            UserPreferences(
                darkThemeConfig = preferences[PreferencesKeys.DARK_THEME_CONFIG]
                    ?.let { runCatching { DarkThemeConfig.valueOf(it) }.getOrNull() }
                    ?: DarkThemeConfig.FOLLOW_SYSTEM,
                colorPalette = preferences[PreferencesKeys.COLOR_PALETTE]
                    ?.let { runCatching { ColorPalette.valueOf(it) }.getOrNull() }
                    ?: ColorPalette.CYAN,
                voiceInputMode = preferences[PreferencesKeys.VOICE_INPUT_MODE]
                    ?.let { runCatching { VoiceInputMode.valueOf(it) }.getOrNull() }
                    ?: VoiceInputMode.LIVE,
                yandexIamToken = preferences[PreferencesKeys.YANDEX_IAM_TOKEN] ?: "",
                yandexFolderId = preferences[PreferencesKeys.YANDEX_FOLDER_ID] ?: "",
            )
        }

    suspend fun setColorPalette(colorPalette: ColorPalette) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.COLOR_PALETTE] = colorPalette.name
        }
    }

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_THEME_CONFIG] = darkThemeConfig.name
        }
    }

    suspend fun setVoiceInputMode(mode: VoiceInputMode) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.VOICE_INPUT_MODE] = mode.name
        }
    }

    suspend fun setYandexTokens(iamToken: String, folderId: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.YANDEX_IAM_TOKEN] = iamToken
            preferences[PreferencesKeys.YANDEX_FOLDER_ID] = folderId
        }
    }
}
