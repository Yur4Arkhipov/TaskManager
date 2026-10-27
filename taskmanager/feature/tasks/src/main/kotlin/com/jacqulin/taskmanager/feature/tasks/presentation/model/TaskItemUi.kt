package com.jacqulin.taskmanager.feature.tasks.presentation.model

import com.jacqulin.taskmanager.core.model.Tag

data class TaskItemUi(
    val id: Int,
    val title: String,
    val createdAtMillis: Long,
    val isCompleted: Boolean,
    val durationMinutes: Int? = null,
    val tags: List<Tag> = emptyList(),
    val isPlannedForToday: Boolean = false,
    val dayPlanOrder: Int? = null,
) {
    val hasDuration: Boolean get() = durationMinutes != null
}
