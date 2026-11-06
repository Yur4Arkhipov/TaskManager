package com.jacqulin.taskmanager.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import com.jacqulin.taskmanager.feature.home.navigation.HomeBaseRoute
import com.jacqulin.taskmanager.feature.home.navigation.HomeRoute
import com.jacqulin.taskmanager.feature.home.navigation.homeSection
import com.jacqulin.taskmanager.feature.home.navigation.navigateToDayPlanRoute
import com.jacqulin.taskmanager.feature.notes.navigation.NotesBaseRoute
import com.jacqulin.taskmanager.feature.notes.navigation.NotesRoute
import com.jacqulin.taskmanager.feature.notes.navigation.navigateToNoteEditor
import com.jacqulin.taskmanager.feature.notes.navigation.notesSection
import com.jacqulin.taskmanager.feature.settings.navigation.SettingsBaseRoute
import com.jacqulin.taskmanager.feature.settings.navigation.SettingsRoute
import com.jacqulin.taskmanager.feature.settings.navigation.navigateToTags
import com.jacqulin.taskmanager.feature.settings.navigation.settingsSection
import com.jacqulin.taskmanager.feature.tasks.navigation.TasksBaseRoute
import com.jacqulin.taskmanager.feature.tasks.navigation.TasksRoute
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
        modifier = modifier,
        enterTransition = {
            topLevelEnterTransition()
        },
        exitTransition = {
            topLevelExitTransition()
        },
        popEnterTransition = {
            topLevelEnterTransition()
        },
        popExitTransition = {
            topLevelExitTransition()
        }
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
            onNavigateToTags = { navController.navigateToTags() },
            onNavigateToNoteEditor = {
                navController.navigateToNoteEditor()
            },
            onNavigateToDayPlan = { navController.navigateToDayPlanRoute() },
            onBackFromDayPlan = { navController.popBackStack() },
        )
    }
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.topLevelEnterTransition(): EnterTransition {
    val from = initialState.destination.getNavigationPosition()
    val to = targetState.destination.getNavigationPosition()

    if (from == null || to == null) {
        return EnterTransition.None
    }

    val direction = getNavigationDirection(from, to)

    return when (direction) {
        1 -> slideInHorizontally(
            animationSpec = tween(300),
            initialOffsetX = { it }
        )

        -1 -> slideInHorizontally(
            animationSpec = tween(300),
            initialOffsetX = { -it }
        )

        else -> EnterTransition.None
    }
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.topLevelExitTransition(): ExitTransition {
    val from = initialState.destination.getNavigationPosition()
    val to = targetState.destination.getNavigationPosition()

    if (from == null || to == null) {
        return ExitTransition.None
    }

    val direction = getNavigationDirection(from, to)

    return when (direction) {
        1 -> slideOutHorizontally(
            animationSpec = tween(300),
            targetOffsetX = { -it }
        )

        -1 -> slideOutHorizontally(
            animationSpec = tween(300),
            targetOffsetX = { it }
        )

        else -> ExitTransition.None
    }
}

private data class NavigationPosition(
    val sectionOrder: Int,
    val depth: Int
)

private fun NavDestination.getNavigationPosition(): NavigationPosition? {
    val sectionOrder = hierarchy.firstNotNullOfOrNull { destination ->
        when {
            destination.hasRoute<HomeBaseRoute>() ->
                TopLevelDestination.HOME.order

            destination.hasRoute<TasksBaseRoute>() ->
                TopLevelDestination.TASKS.order
            destination.hasRoute<NotesBaseRoute>() ->
                TopLevelDestination.NOTES.order

            destination.hasRoute<SettingsBaseRoute>() ->
                TopLevelDestination.SETTINGS.order

            else -> null
        }
    } ?: return null

    val depth = when {
        hasRoute<HomeRoute>() ||
            hasRoute<TasksRoute>() ||
            hasRoute<NotesRoute>() ||
            hasRoute<SettingsRoute>() -> 0

        else -> 1
    }

    return NavigationPosition(
        sectionOrder = sectionOrder,
        depth = depth
    )
}

private fun getNavigationDirection(
    from: NavigationPosition,
    to: NavigationPosition
): Int {
    return when {
        to.sectionOrder > from.sectionOrder -> 1
        to.sectionOrder < from.sectionOrder -> -1

        to.depth > from.depth -> 1
        to.depth < from.depth -> -1

        else -> 0
    }
}
