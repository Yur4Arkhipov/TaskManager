package com.jacqulin.taskmanager.feature.notes.presentation.noteeditor

sealed interface NoteEditorEffect {
    data object NavigateBack : NoteEditorEffect
    data object LaunchGallery : NoteEditorEffect
    data object LaunchCamera : NoteEditorEffect
    data class ShowError(val message: String) : NoteEditorEffect
}
