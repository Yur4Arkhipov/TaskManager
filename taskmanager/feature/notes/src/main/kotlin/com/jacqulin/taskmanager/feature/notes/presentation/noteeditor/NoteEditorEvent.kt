package com.jacqulin.taskmanager.feature.notes.presentation.noteeditor

import android.net.Uri

sealed interface NoteEditorEvent {
    data class TitleChanged(val value: String) : NoteEditorEvent
    data class ContentChanged(val value: String) : NoteEditorEvent
    data class ImageSelected(val uri: Uri) : NoteEditorEvent
    data class CameraCancelled(val uri: Uri) : NoteEditorEvent
    data object ImageAddFromGalleryClicked : NoteEditorEvent
    data object ImageAddFromCameraClicked : NoteEditorEvent
    data object CameraPermissionGranted : NoteEditorEvent
    data object CameraPermissionDenied : NoteEditorEvent
    data object ImageRemoved : NoteEditorEvent

    data object VoiceInputStartClicked : NoteEditorEvent
    data object VoiceInputStopClicked : NoteEditorEvent
    data object VoicePermissionGranted : NoteEditorEvent
    data object VoicePermissionDenied : NoteEditorEvent
    data class VoiceTextRecognized(val text: String) : NoteEditorEvent
    data object VoiceInputDismissed : NoteEditorEvent
    data object VoiceInputRetry : NoteEditorEvent
    data object VoiceInputCancel : NoteEditorEvent

    data object SaveClicked : NoteEditorEvent
    data object BackClicked : NoteEditorEvent
}
