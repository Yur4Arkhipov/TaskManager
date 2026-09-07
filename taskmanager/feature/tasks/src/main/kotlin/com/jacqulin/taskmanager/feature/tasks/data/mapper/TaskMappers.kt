package com.jacqulin.taskmanager.feature.tasks.data.mapper

import com.jacqulin.taskmanager.core.database.entity.TaskEntity
import com.jacqulin.taskmanager.feature.tasks.domain.model.Task

fun TaskEntity.toDomain(): Task {
    return Task(
        id = id,
        title = title,
        createdAtMillis = createdAtMillis,
        isCompleted = isCompleted
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        createdAtMillis = createdAtMillis,
        isCompleted = isCompleted
    )
}
