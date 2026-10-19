package com.jacqulin.taskmanager.core.data.repository

import com.jacqulin.taskmanager.core.data.model.YandexTokens
import com.jacqulin.taskmanager.core.datastore.UserPreferencesDataSource
import com.jacqulin.taskmanager.core.model.ColorPalette
import com.jacqulin.taskmanager.core.model.DarkThemeConfig
import com.jacqulin.taskmanager.core.model.UserData
import com.jacqulin.taskmanager.core.model.VoiceInputMode
import com.jacqulin.taskmanager.core.security.SecureTokenStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class UserDataRepositoryImpl @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
    private val secureTokenStore: SecureTokenStorage,
) : UserDataRepository {

    override val userData: Flow<UserData> =
        userPreferencesDataSource.userPreferences
            .map {
                UserData(
                    darkThemeConfig = it.darkThemeConfig,
                    colorPalette = it.colorPalette,
                    voiceInputMode = it.voiceInputMode,
                )
            }

    override suspend fun setDarkThemeConfig(config: DarkThemeConfig) =
        userPreferencesDataSource.setDarkThemeConfig(config)

    override suspend fun setColorPalette(palette: ColorPalette) =
        userPreferencesDataSource.setColorPalette(palette)

    override suspend fun setVoiceInputMode(mode: VoiceInputMode) =
        userPreferencesDataSource.setVoiceInputMode(mode)

    override suspend fun setYandexTokens(iamToken: String, folderId: String) {
        secureTokenStore.setToken(YANDEX_IAM_TOKEN, iamToken)
        secureTokenStore.setToken(YANDEX_FOLDER_ID, folderId)
    }

    override suspend fun getYandexTokens(): YandexTokens {
        return YandexTokens(
            iamToken = secureTokenStore.getToken(YANDEX_IAM_TOKEN).orEmpty(),
            folderId = secureTokenStore.getToken(YANDEX_FOLDER_ID).orEmpty(),
        )
    }

    private companion object {
        const val YANDEX_IAM_TOKEN = "yandex.iam_token"
        const val YANDEX_FOLDER_ID = "yandex.folder_id"
    }
}
