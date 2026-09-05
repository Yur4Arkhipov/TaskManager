package com.jacqulin.taskmanager.feature.notes.presentation.notebase

sealed interface NotesEffect {
    data object NavigateToCreateNote : NotesEffect
    data class NavigateToExistingNote(val noteId: String) : NotesEffect
}
