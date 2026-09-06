package com.jacqulin.taskmanager.feature.notes.presentation.noteeditor

sealed interface NoteEditorEffect {
    data object NavigateBack : NoteEditorEffect
    data class ShowError(val message: String) : NoteEditorEffect
}
