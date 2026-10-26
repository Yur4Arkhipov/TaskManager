package com.jacqulin.taskmanager.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.jacqulin.taskmanager.feature.home.presentation.HomeScreen
import com.jacqulin.taskmanager.feature.home.presentation.dayplan.DayPlanScreen
import kotlinx.serialization.Serializable

@Serializable
data object HomeBaseRoute

@Serializable
data object HomeRoute

@Serializable
data object DayPlanRoute

fun NavController.navigateToHome(navOptions: NavOptions) = navigate(route = HomeRoute, navOptions)

fun NavController.navigateToDayPlanRoute() = navigate(route = DayPlanRoute)

fun NavGraphBuilder.homeSection(
    onNavigateToTasks: () -> Unit,
    onNavigateToNoteEditor: () -> Unit,
    onNavigateToDayPlan: () -> Unit,
    onBackFromDayPlan: () -> Unit,
) {
    navigation<HomeBaseRoute>(startDestination = HomeRoute) {
        composable<HomeRoute> {
            HomeScreen(
                onNavigateToTasks = onNavigateToTasks,
                onNavigateToNotes = onNavigateToNoteEditor,
                onNavigateToFullPlan = onNavigateToDayPlan,
            )
        }
        composable<DayPlanRoute> {
            DayPlanScreen(
                onBackClick = onBackFromDayPlan,
            )
        }
    }
}
