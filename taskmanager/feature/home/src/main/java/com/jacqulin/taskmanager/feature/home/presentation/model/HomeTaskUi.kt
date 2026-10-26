package com.jacqulin.taskmanager.feature.home.presentation.model

import com.jacqulin.taskmanager.core.model.Tag
import com.jacqulin.taskmanager.core.model.Task

data class HomeTaskUi(
    val id: Int = 0,
    val title: String = "",
    val createdAtMillis: Long = 0,
    val isCompleted: Boolean = false,
    val tags: List<Tag> = emptyList(),
    val isPlannedForToday: Boolean = false,
    val durationMinutes: Int? = null,
) {
    val hasDuration: Boolean get() = durationMinutes != null
}

fun HomeTaskUi.toDomain(): Task = Task(
    id = id,
    title = title,
    createdAtMillis = createdAtMillis,
    isCompleted = isCompleted,
    tags = tags,
    isPlannedForToday = isPlannedForToday
)
