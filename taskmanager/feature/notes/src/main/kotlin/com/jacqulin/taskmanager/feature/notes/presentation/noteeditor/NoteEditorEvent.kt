package com.jacqulin.taskmanager.feature.notes.presentation.noteeditor

import android.net.Uri

sealed interface NoteEditorEvent {
    data class TitleChanged(val value: String) : NoteEditorEvent
    data class ContentChanged(val value: String) : NoteEditorEvent
    data class ImageSelected(val uri: Uri) : NoteEditorEvent
    data object ImageAddFromGalleryClicked : NoteEditorEvent
    data object ImageAddFromCameraClicked : NoteEditorEvent
    data object ImageRemoved : NoteEditorEvent
    data object SaveClicked : NoteEditorEvent
    data object BackClicked : NoteEditorEvent
}
