package com.jacqulin.taskmanager.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.util.trace
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.designsystem.model.TopAppBarConfig
import com.jacqulin.taskmanager.feature.notes.navigation.NoteEditorRoute
import com.jacqulin.taskmanager.feature.notes.navigation.NotesRoute
import com.jacqulin.taskmanager.feature.notes.navigation.navigateToNotes
import com.jacqulin.taskmanager.feature.settings.navigation.navigateToSettings
import com.jacqulin.taskmanager.feature.tasks.navigation.navigateToTasks
import com.jacqulin.taskmanager.navigation.TopLevelDestination

@Composable
fun rememberAppState(
//    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): AppState {
    return remember(
        navController,
//        coroutineScope,
    ) {
        AppState(
            navController = navController,
//            coroutineScope = coroutineScope,
        )
    }
}

class AppState(
    val navController: NavHostController,
//    coroutineScope: CoroutineScope
) {

    private val previousDestination = mutableStateOf<NavDestination?>(null)

    val currentDestination: NavDestination?
        @Composable get() {
            val currentEntry = navController.currentBackStackEntryFlow
                .collectAsState(initial = null)

            return currentEntry.value?.destination.also { destination ->
                if (destination != null) {
                    previousDestination.value = destination
                }
            } ?: previousDestination.value
        }

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() {
            return TopLevelDestination.entries.firstOrNull { topLevelDestination ->
                currentDestination?.hasRoute(topLevelDestination.route) == true
            }
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    val currentTopAppBarConfig: TopAppBarConfig?
        @Composable get() {
            return when {
                currentDestination?.hasRoute(NotesRoute::class) == true -> {
                    TopAppBarConfig(
                        titleRes = R.string.notes_title,
                    )
                }
                currentDestination?.hasRoute(NoteEditorRoute::class) == true -> {
                    TopAppBarConfig(
                        titleRes = R.string.notes_edit_note,
                        navigationIcon = painterResource(R.drawable.ic_arrow_back_left),
                        onNavigationClick = {
                            navController.popBackStack()
                        },
                        actionIcon = painterResource(R.drawable.ic_save),
                        onActionClick = {

                        },
                    )
                }
                else -> null
            }
        }

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        trace("Navigation: ${topLevelDestination.name}") {
            val topLevelNavOptions = navOptions {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }

            when (topLevelDestination) {
                TopLevelDestination.NOTES -> navController.navigateToNotes(topLevelNavOptions)
                TopLevelDestination.TASKS -> navController.navigateToTasks(topLevelNavOptions)
                TopLevelDestination.SETTINGS -> navController.navigateToSettings(topLevelNavOptions)
            }
        }
    }
}
