package com.jacqulin.taskmanager.feature.notes.presentation.notebase

sealed interface NotesEvent {
    data class OnSearchQueryChanged(val query: String) : NotesEvent
    data object OnSearchSubmitted : NotesEvent
    data class OnSortChanged(val sortType: NotesSortType) : NotesEvent
    data object OnDeleteModeToggled : NotesEvent
    data class OnDeleteNoteClicked(val noteId: String) : NotesEvent
    data class OnNoteClicked(val noteId: String) : NotesEvent
    data object OnCreateNoteClicked : NotesEvent
}
