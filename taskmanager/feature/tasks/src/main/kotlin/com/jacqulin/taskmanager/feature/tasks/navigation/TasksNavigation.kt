package com.jacqulin.taskmanager.feature.tasks.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.jacqulin.taskmanager.feature.tasks.presentation.TasksScreen
import kotlinx.serialization.Serializable

@Serializable
data object TasksBaseRoute

@Serializable
data object TasksRoute

fun NavController.navigateToTasks(navOptions: NavOptions) = navigate(route = TasksRoute, navOptions)

fun NavGraphBuilder.tasksSection(
) {
    navigation<TasksBaseRoute>(startDestination = TasksRoute) {
        composable<TasksRoute> {
            TasksScreen()
        }
    }
}
