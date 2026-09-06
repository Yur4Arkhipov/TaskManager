package com.jacqulin.taskmanager.feature.notes.presentation.noteeditor

data class NoteEditorUiState(
    val title: String = "",
    val titleError: String? = null,
    val content: String = "",
    val hasImage: Boolean = false,
    val isLoading: Boolean = true
)
