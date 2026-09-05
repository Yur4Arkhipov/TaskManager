package com.jacqulin.taskmanager.feature.notes.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.jacqulin.taskmanager.feature.notes.presentation.noteeditor.NoteEditorScreen
import com.jacqulin.taskmanager.feature.notes.presentation.notebase.NotesScreen
import kotlinx.serialization.Serializable

@Serializable
data object NotesBaseRoute

@Serializable
data object NotesRoute

@Serializable
data class NoteEditorRoute(
    val noteId: String? = null,
//    val mode: NoteEditorMode = NoteEditorMode.VIEW
)

fun NavController.navigateToNotes(navOptions: NavOptions) = navigate(route = NotesRoute, navOptions)

fun NavController.navigateToNoteEditor() = navigate(route = NoteEditorRoute())

fun NavController.navigateToNoteEditor(noteId: String) = navigate(route = NoteEditorRoute(noteId = noteId))

fun NavGraphBuilder.notesSection(
    onNavigateToNoteEditor: () -> Unit,
//    onNavigateToExistingNote: (String) -> Unit,
    onBackClick: () -> Unit

) {
    navigation<NotesBaseRoute>(startDestination = NotesRoute) {
        composable<NotesRoute> {
            NotesScreen(
                onAddClick = onNavigateToNoteEditor,
//                onNoteClick = onNavigateToExistingNote
            )
        }

        composable<NoteEditorRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<NoteEditorRoute>()
            NoteEditorScreen(
//                noteId = route.noteId
                onBack = onBackClick
            )
        }
    }
}
