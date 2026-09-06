package com.jacqulin.taskmanager.feature.notes.presentation.notebase

sealed interface NotesEvent {
    data class OnSearchQueryChanged(val query: String) : NotesEvent
    data object OnSearchSubmitted : NotesEvent
    data class OnSortChanged(val sortType: NotesSortType) : NotesEvent
    data object OnDeleteModeToggled : NotesEvent
    data class OnDeleteNoteClicked(val noteId: Int) : NotesEvent
    data class OnNoteClicked(val noteId: Int) : NotesEvent
    data object OnCreateNoteClicked : NotesEvent
}
