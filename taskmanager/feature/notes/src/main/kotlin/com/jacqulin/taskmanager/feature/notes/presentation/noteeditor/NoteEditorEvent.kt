package com.jacqulin.taskmanager.feature.notes.presentation.noteeditor

sealed interface NoteEditorEvent {
    data class TitleChanged(val value: String) : NoteEditorEvent
    data class ContentChanged(val value: String) : NoteEditorEvent
    data object ImageAdded : NoteEditorEvent
    data object ImageRemoved : NoteEditorEvent
    data object SaveClicked : NoteEditorEvent
    data object BackClicked : NoteEditorEvent
}
