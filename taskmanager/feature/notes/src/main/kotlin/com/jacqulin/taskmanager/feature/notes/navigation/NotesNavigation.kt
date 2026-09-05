package com.jacqulin.taskmanager.feature.notes.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable
data object NotesBaseRoute

@Serializable
data object NotesRoute

fun NavController.navigateToNotes(navOptions: NavOptions) = navigate(route = NotesRoute, navOptions)

fun NavGraphBuilder.notesSection(
) {
    navigation<NotesBaseRoute>(startDestination = NotesRoute) {
        composable< NotesRoute>() {
        }
    }
}