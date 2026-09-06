package com.jacqulin.taskmanager.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val content: String,
    val createdAtMillis: Long,
    val hasPreviewImage: Boolean,
)
