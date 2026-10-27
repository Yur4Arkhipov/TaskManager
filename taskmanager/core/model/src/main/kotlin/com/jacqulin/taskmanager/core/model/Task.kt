package com.jacqulin.taskmanager.core.model

data class Task(
    val id: Int,
    val title: String,
    val createdAtMillis: Long,
    val isCompleted: Boolean,
    val tags: List<Tag>,
    val isPlannedForToday: Boolean,
    val dayPlanOrder: Int?,
)
