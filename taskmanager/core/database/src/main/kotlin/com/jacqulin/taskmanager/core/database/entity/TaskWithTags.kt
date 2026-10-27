package com.jacqulin.taskmanager.core.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.jacqulin.taskmanager.core.model.Task
import com.jacqulin.taskmanager.core.model.Tag

data class TaskWithTags(
    @Embedded
    val task: TaskEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = TaskTagCrossRef::class,
            parentColumn = "taskId",
            entityColumn = "tagId",
        ),
    )
    val tags: List<TagEntity>,
)

fun TaskWithTags.asExternalModel(): Task = Task(
    id = task.id,
    title = task.title,
    createdAtMillis = task.createdAtMillis,
    isCompleted = task.isCompleted,
    tags = tags.map { Tag(name = it.title, id = it.id) },
    isPlannedForToday = task.isPlannedForToday,
    dayPlanOrder = task.dayPlanOrder,
)

fun Task.toEntity(): TaskEntity = TaskEntity(
    id = id,
    title = title,
    createdAtMillis = createdAtMillis,
    isCompleted = isCompleted,
    isPlannedForToday = isPlannedForToday,
    dayPlanOrder = dayPlanOrder,
)
