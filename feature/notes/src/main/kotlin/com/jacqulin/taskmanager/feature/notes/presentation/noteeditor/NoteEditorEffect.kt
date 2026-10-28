package com.jacqulin.taskmanager.feature.notes.presentation.noteeditor

import android.net.Uri

sealed interface NoteEditorEffect {
    data object NavigateBack : NoteEditorEffect
    data object LaunchGallery : NoteEditorEffect
    data class LaunchCamera(val uri: Uri) : NoteEditorEffect
    data object RequestCameraPermission : NoteEditorEffect
    data object RequestVoicePermission : NoteEditorEffect
    data class ShowError(val message: String) : NoteEditorEffect
}
