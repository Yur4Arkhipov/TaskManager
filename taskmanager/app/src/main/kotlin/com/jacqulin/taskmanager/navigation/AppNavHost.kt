package com.jacqulin.taskmanager.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.jacqulin.taskmanager.feature.notes.navigation.NotesBaseRoute
import com.jacqulin.taskmanager.feature.notes.navigation.navigateToNoteEditor
import com.jacqulin.taskmanager.feature.notes.navigation.notesSection
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
        startDestination = NotesBaseRoute,
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
        tasksSection()
        settingsSection()
    }
}
