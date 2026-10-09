package com.jacqulin.taskmanager.data.repository

import com.jacqulin.taskmanager.core.model.ColorPalette
import com.jacqulin.taskmanager.core.model.DarkThemeConfig
import com.jacqulin.taskmanager.core.model.UserData
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
}
