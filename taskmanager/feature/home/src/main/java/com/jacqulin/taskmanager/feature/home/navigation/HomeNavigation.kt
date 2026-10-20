package com.jacqulin.taskmanager.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.jacqulin.taskmanager.feature.home.presentation.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
data object HomeBaseRoute

@Serializable
data object HomeRoute

fun NavController.navigateToHome(navOptions: NavOptions) = navigate(route = HomeRoute, navOptions)

fun NavGraphBuilder.homeSection(
    onNavigateToTasks: () -> Unit,
    onNavigateToCreateNote: () -> Unit,
) {
    navigation<HomeBaseRoute>(startDestination = HomeRoute) {
        composable<HomeRoute> {
            HomeScreen(
                onNavigateToTasks = onNavigateToTasks,
                onNavigateToNotes = onNavigateToCreateNote,
            )
        }
    }
}
