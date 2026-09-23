package com.jacqulin.taskmanager.feature.notes.presentation.notebase

import com.jacqulin.taskmanager.core.designsystem.model.SortType
import com.jacqulin.taskmanager.feature.notes.presentation.model.NoteListItemUi

data class NotesUiState(
    val searchQueryInput: String = "",
    val appliedSearchQuery: String = "",
    val sortType: SortType = SortType.NEW_TO_OLD,
    val isDeleteModeEnabled: Boolean = false,
    val visibleNotes: List<NoteListItemUi> = emptyList(),
    val isEmpty: Boolean = true
)
