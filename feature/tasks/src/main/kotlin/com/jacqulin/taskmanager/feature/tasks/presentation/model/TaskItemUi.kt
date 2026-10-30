package com.jacqulin.taskmanager.feature.tasks.presentation.model

import com.jacqulin.taskmanager.core.model.Tag
import com.jacqulin.taskmanager.core.model.Task

data class TaskItemUi(
    val id: Int,
    val title: String,
    val createdAtMillis: Long,
    val isCompleted: Boolean,
    val tags: List<Tag> = emptyList(),
    val isPlannedForToday: Boolean = false,
    val dayPlanOrder: Int? = null,
)

fun Task.toUiModel(): TaskItemUi = TaskItemUi(
    id = id,
    title = title,
    createdAtMillis = createdAtMillis,
    isCompleted = isCompleted,
    tags = tags,
    isPlannedForToday = isPlannedForToday,
    dayPlanOrder = dayPlanOrder,
)

fun TaskItemUi.toDomain(): Task = Task(
    id = id,
    title = title,
    createdAtMillis = createdAtMillis,
    isCompleted = isCompleted,
    tags = tags,
    isPlannedForToday = isPlannedForToday,
    dayPlanOrder = dayPlanOrder,
)
