package com.jacqulin.taskmanager.feature.notes.domain.model

data class Note(
    val id: String,
    val title: String,
    val createdAtMillis: Long,
    val hasPreviewImage: Boolean
)
