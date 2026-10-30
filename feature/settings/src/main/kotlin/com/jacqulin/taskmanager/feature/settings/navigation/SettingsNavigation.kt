package com.jacqulin.taskmanager.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.jacqulin.taskmanager.feature.settings.presentation.screens.SettingsScreen
import com.jacqulin.taskmanager.feature.settings.presentation.screens.TagsScreen
import kotlinx.serialization.Serializable

@Serializable
data object SettingsBaseRoute

@Serializable
data object SettingsRoute

@Serializable
data object TagsRoute

fun NavController.navigateToSettings(navOptions: NavOptions) = navigate(route = SettingsRoute, navOptions)

fun NavHostController.navigateToTags() = navigate(TagsRoute)

fun NavGraphBuilder.settingsSection(
    onNavigateToTags: () -> Unit,
    onBackClick: () -> Unit,
) {
    navigation<SettingsBaseRoute>(startDestination = SettingsRoute) {
        composable<SettingsRoute> {
            SettingsScreen(onNavigateToTags = onNavigateToTags)
        }
        composable<TagsRoute> {
            TagsScreen(onBackClick = onBackClick)
        }
    }
}
