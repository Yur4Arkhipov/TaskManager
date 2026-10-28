package com.jacqulin.taskmanager.feature.home.navigation

import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.jacqulin.taskmanager.feature.home.presentation.HomeScreen
import com.jacqulin.taskmanager.feature.home.presentation.HomeViewModel
import com.jacqulin.taskmanager.feature.home.presentation.components.FocusSession
import com.jacqulin.taskmanager.feature.home.presentation.dayplan.DayPlanScreen
import com.jacqulin.taskmanager.feature.home.presentation.focus.FocusConfigScreen
import com.jacqulin.taskmanager.feature.home.presentation.model.FocusMode
import kotlinx.serialization.Serializable

@Serializable
data object HomeBaseRoute

@Serializable
data object HomeRoute

@Serializable
data object DayPlanRoute

@Serializable
data class FocusConfigRoute(
    val taskId: Int
)

@Serializable
data class FocusSessionRoute(
    val mode: FocusMode
)

fun NavController.navigateToHome(navOptions: NavOptions) = navigate(route = HomeRoute, navOptions)

fun NavController.navigateToDayPlanRoute() = navigate(route = DayPlanRoute)

fun NavController.navigateToFocusConfig(taskId: Int) = navigate(route = FocusConfigRoute(taskId = taskId))

fun NavController.navigateToFocusSession(mode: FocusMode) = navigate(route = FocusSessionRoute(mode = mode))

fun NavGraphBuilder.homeSection(
    navController: NavController,
    onNavigateToTasks: () -> Unit,
    onNavigateToNoteEditor: () -> Unit,
    onNavigateToDayPlan: () -> Unit,
    onBackFromDayPlan: () -> Unit,
    onNavigateToFocusConfig: (Int) -> Unit,
    onBackFromFocusConfig: () -> Unit,
    onNavigateToFocusSession: (FocusMode) -> Unit,
    onBackFromFocusSession: () -> Unit
) {
    navigation<HomeBaseRoute>(startDestination = HomeRoute) {
        composable<HomeRoute> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(HomeBaseRoute)
            }
            val viewModel: HomeViewModel = hiltViewModel(parentEntry)
            HomeScreen(
                onNavigateToTasks = onNavigateToTasks,
                onNavigateToNotes = onNavigateToNoteEditor,
                onNavigateToFullPlan = onNavigateToDayPlan,
                onNavigateToFocusConfig = onNavigateToFocusConfig,
                viewModel = viewModel,
            )
        }
        composable<DayPlanRoute> { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(HomeBaseRoute)
            }
            val viewModel: HomeViewModel = hiltViewModel(parentEntry)
            DayPlanScreen(
                onBackClick = onBackFromDayPlan,
                viewModel = viewModel,
            )
        }
        composable<FocusConfigRoute> {
            FocusConfigScreen(
                onBackClick = onBackFromFocusConfig,
                onNavigateToFocusSession = onNavigateToFocusSession
            )
        }
        composable<FocusSessionRoute> {
            FocusSession(
                onBackClick = onBackFromFocusSession,
            )
        }
    }
}
