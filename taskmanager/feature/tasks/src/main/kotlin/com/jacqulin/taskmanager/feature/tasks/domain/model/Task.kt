package com.jacqulin.taskmanager.feature.tasks.domain.model

data class Task(
    val id: Int,
    val title: String,
    val createdAtMillis: Long,
    val isCompleted: Boolean
)
