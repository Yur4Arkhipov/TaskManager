package com.jacqulin.taskmanager.feature.notes.presentation.model

data class NoteListItemUi(
    val id: Int,
    val title: String,
    val createdAtMillis: Long,
    val hasPreviewImage: Boolean,
    val content: String = ""
)
