package com.jacqulin.taskmanager.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.jacqulin.taskmanager.feature.home.navigation.HomeBaseRoute
import com.jacqulin.taskmanager.feature.home.navigation.homeSection
import com.jacqulin.taskmanager.feature.home.navigation.navigateToDayPlanRoute
import com.jacqulin.taskmanager.feature.home.navigation.navigateToFocusConfig
import com.jacqulin.taskmanager.feature.home.navigation.navigateToFocusSession
import com.jacqulin.taskmanager.feature.notes.navigation.navigateToNoteEditor
import com.jacqulin.taskmanager.feature.notes.navigation.notesSection
import com.jacqulin.taskmanager.feature.settings.navigation.navigateToTags
import com.jacqulin.taskmanager.feature.settings.navigation.settingsSection
import com.jacqulin.taskmanager.feature.tasks.navigation.tasksSection
import com.jacqulin.taskmanager.ui.AppState

@Composable
fun AppNavHost(
    appState: AppState,
    modifier: Modifier = Modifier
) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = HomeBaseRoute,
        modifier = modifier
    ) {
        notesSection(
            onNavigateToNoteEditor = {
                navController.navigateToNoteEditor()
            },
            onNavigateToExistingNote = { noteId ->
                navController.navigateToNoteEditor(noteId)
            },
            onBackClick = {
                navController.popBackStack()
            }
        )
        tasksSection(
            onNavigateToTags = { navController.navigateToTags() }
        )
        settingsSection(
            onNavigateToTags = { navController.navigateToTags() },
            onBackClick = { navController.popBackStack() },
        )
        homeSection(
            navController = navController,
            onNavigateToTasks = {

            },
            onNavigateToNoteEditor = {
                navController.navigateToNoteEditor()
            },
            onNavigateToDayPlan = { navController.navigateToDayPlanRoute() },
            onBackFromDayPlan = { navController.popBackStack() },
            onBackFromFocusConfig = { navController.popBackStack() },
            onNavigateToFocusConfig = { taskId -> navController.navigateToFocusConfig(taskId) },
            onNavigateToFocusSession = { sessionId -> navController.navigateToFocusSession(sessionId) },
            onBackFromFocusSession = { navController.popBackStack() }
        )
    }
}
