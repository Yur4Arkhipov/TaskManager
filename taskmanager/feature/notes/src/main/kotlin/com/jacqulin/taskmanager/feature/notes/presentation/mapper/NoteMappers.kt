package com.jacqulin.taskmanager.feature.notes.presentation.mapper

import com.jacqulin.taskmanager.feature.notes.domain.model.Note
import com.jacqulin.taskmanager.feature.notes.presentation.model.NoteListItemUi

fun Note.toUiModel(): NoteListItemUi = NoteListItemUi(
    id = id,
    title = title,
    content = content,
    createdAtMillis = createdAtMillis,
    hasPreviewImage = hasPreviewImage
)
