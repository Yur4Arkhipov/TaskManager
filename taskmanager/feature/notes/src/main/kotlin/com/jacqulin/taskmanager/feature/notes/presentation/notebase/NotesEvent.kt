package com.jacqulin.taskmanager.feature.notes.presentation.notebase

import com.jacqulin.taskmanager.designsystem.model.SortType

sealed interface NotesEvent {
    data class OnSearchQueryChanged(val query: String) : NotesEvent
    data object OnSearchSubmitted : NotesEvent
    data class OnSortChanged(val sortType: SortType) : NotesEvent
    data object OnDeleteModeToggled : NotesEvent
    data class OnDeleteNoteClicked(val noteId: Int) : NotesEvent
    data class OnNoteClicked(val noteId: Int) : NotesEvent
    data object OnCreateNoteClicked : NotesEvent
}
