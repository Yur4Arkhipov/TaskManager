package com.jacqulin.taskmanager.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import com.jacqulin.taskmanager.feature.notes.navigation.NotesBaseRoute
import com.jacqulin.taskmanager.feature.notes.navigation.navigateToNoteEditor
import com.jacqulin.taskmanager.feature.notes.navigation.notesSection
import com.jacqulin.taskmanager.feature.settings.navigation.settingsSection
import com.jacqulin.taskmanager.feature.tasks.navigation.tasksSection
import com.jacqulin.taskmanager.navigation.logging.NavigationLoggerEntryPoint
import com.jacqulin.taskmanager.ui.AppState
import dagger.hilt.android.EntryPointAccessors

@Composable
fun AppNavHost(
    appState: AppState,
    modifier: Modifier = Modifier
) {
    val navController = appState.navController

    val context = LocalContext.current

    val logger = remember(context) {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            NavigationLoggerEntryPoint::class.java,
        ).navigationLogger()
    }

    var previousRoute by remember { mutableStateOf<String?>(null) }

    DisposableEffect(navController, logger) {
        val listener =
            NavController.OnDestinationChangedListener { _, destination, arguments ->
                val toRoute = destination.route ?: destination.navigatorName

                logger.logNavigation(
                    fromRoute = previousRoute,
                    toRoute = toRoute,
                    arguments = arguments,
                )

                previousRoute = toRoute
            }

        navController.addOnDestinationChangedListener(listener)

        onDispose { navController.removeOnDestinationChangedListener(listener) }
    }

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
