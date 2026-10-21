package com.jacqulin.taskmanager.feature.tasks.presentation.state

data class TaskState(
    val title: String = "",
    val tags: List<String> = emptyList(),
    val isPlannedForToday: Boolean = false,
)
