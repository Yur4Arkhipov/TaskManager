package com.jacqulin.taskmanager.feature.notes.data.mapper

import com.jacqulin.taskmanager.core.database.entity.NoteEntity
import com.jacqulin.taskmanager.feature.notes.domain.model.Note

fun NoteEntity.toDomain(): Note {
    return Note(
        id = id,
        title = title,
        content = content,
        createdAtMillis = createdAtMillis,
        hasPreviewImage = hasPreviewImage
    )
}

fun Note.toEntity(): NoteEntity {
    return NoteEntity(
        id = id,
        title = title,
        content = content,
        createdAtMillis = createdAtMillis,
        hasPreviewImage = hasPreviewImage
    )
}
