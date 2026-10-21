package com.jacqulin.taskmanager.feature.tasks.presentation.model

data class TaskItemUi(
    val id: Int,
    val title: String,
    val createdAtMillis: Long,
    val isCompleted: Boolean,
    val durationMinutes: Int? = null,
    val tags: List<String> = emptyList(),
    val isPlannedForToday: Boolean = false,
) {
    val hasDuration: Boolean get() = durationMinutes != null
}
