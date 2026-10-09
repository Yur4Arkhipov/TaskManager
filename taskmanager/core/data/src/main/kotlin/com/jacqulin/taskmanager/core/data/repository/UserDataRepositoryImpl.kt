package com.jacqulin.taskmanager.core.data.repository

import com.jacqulin.taskmanager.core.datastore.UserPreferencesDataSource
import com.jacqulin.taskmanager.core.model.ColorPalette
import com.jacqulin.taskmanager.core.model.DarkThemeConfig
import com.jacqulin.taskmanager.core.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class UserDataRepositoryImpl @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
) : UserDataRepository {

    override val userData: Flow<UserData> =
        userPreferencesDataSource.userPreferences
            .map {
                UserData(
                    darkThemeConfig = it.darkThemeConfig,
                    colorPalette = it.colorPalette,
                )
            }

    override suspend fun setDarkThemeConfig(config: DarkThemeConfig) =
        userPreferencesDataSource.setDarkThemeConfig(config)

    override suspend fun setColorPalette(palette: ColorPalette) =
        userPreferencesDataSource.setColorPalette(palette)
}
