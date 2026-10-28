package com.jacqulin.taskmanager.feature.home.navigation

import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.jacqulin.taskmanager.feature.home.presentation.screens.DayPlanScreen
import com.jacqulin.taskmanager.feature.home.presentation.screens.HomeScreen
import com.jacqulin.taskmanager.feature.home.presentation.viewmodels.HomeViewModel
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
    navController: NavController,
    onNavigateToTasks: () -> Unit,
    onNavigateToNoteEditor: () -> Unit,
    onNavigateToDayPlan: () -> Unit,
    onBackFromDayPlan: () -> Unit,
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
    }
}
