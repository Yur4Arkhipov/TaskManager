package com.jacqulin.taskmanager.feature.notes.presentation.noteeditor

data class NoteEditorUiState(
    val title: String = "",
    val content: String = "",
    val isLoading: Boolean = false
)
