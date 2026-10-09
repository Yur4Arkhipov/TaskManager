package com.jacqulin.taskmanager.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jacqulin.taskmanager.core.model.Task

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val createdAtMillis: Long,
    val isCompleted: Boolean
)

fun TaskEntity.asExternalModel(): Task {
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
