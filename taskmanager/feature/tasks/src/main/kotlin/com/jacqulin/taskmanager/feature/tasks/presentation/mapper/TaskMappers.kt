package com.jacqulin.taskmanager.feature.tasks.presentation.mapper

import com.jacqulin.taskmanager.core.model.Task
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TaskItemUi

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
