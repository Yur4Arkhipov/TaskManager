package com.jacqulin.taskmanager.feature.tasks.presentation.model

data class TaskItemUi(
    val id: Int,
    val title: String,
    val createdAtMillis: Long,
    val isCompleted: Boolean
)
