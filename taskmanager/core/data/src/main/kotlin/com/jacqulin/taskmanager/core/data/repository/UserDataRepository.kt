package com.jacqulin.taskmanager.core.data.repository

import com.jacqulin.taskmanager.core.model.ColorPalette
import com.jacqulin.taskmanager.core.model.DarkThemeConfig
import com.jacqulin.taskmanager.core.model.UserData
import com.jacqulin.taskmanager.core.model.VoiceInputMode
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    /**
     * Stream of [UserData]
     */
    val userData: Flow<UserData>

    /**
     * Sets the desired theme
     */
    suspend fun setDarkThemeConfig(config: DarkThemeConfig)

    /**
     * Sets the desired color palette
     */
    suspend fun setColorPalette(palette: ColorPalette)

    /**
     * Sets preferred voice input mode
     */
    suspend fun setVoiceInputMode(mode: VoiceInputMode)

    /**
     * Sets Yandex tokens
     */
    suspend fun setYandexTokens(iamToken: String, folderId: String)
}
