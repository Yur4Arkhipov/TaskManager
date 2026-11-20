package com.jacqulin.taskmanager.feature.notes.presentation.model

import com.jacqulin.taskmanager.core.model.Note

data class NoteListItemUi(
    val id: Int,
    val title: String,
    val createdAtMillis: Long,
    val imageUri: String? = null,
    val content: String = ""
)

fun Note.toNoteListItemUi(): NoteListItemUi = NoteListItemUi(
    id = id,
    title = title,
    content = content,
    createdAtMillis = createdAtMillis,
    imageUri = imagePath
)
