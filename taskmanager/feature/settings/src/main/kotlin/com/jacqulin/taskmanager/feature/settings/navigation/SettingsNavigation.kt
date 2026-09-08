package com.jacqulin.taskmanager.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.jacqulin.taskmanager.feature.settings.presentation.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
data object SettingsBaseRoute

@Serializable
data object SettingsRoute

fun NavController.navigateToSettings(navOptions: NavOptions) = navigate(route = SettingsRoute, navOptions)

fun NavGraphBuilder.settingsSection(
    onNavigateBack: () -> Unit = {},
) {
    navigation<SettingsBaseRoute>(startDestination = SettingsRoute) {
        composable<SettingsRoute> {
            SettingsScreen(onNavigateBack = onNavigateBack)
        }
    }
}
