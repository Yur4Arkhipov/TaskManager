package com.jacqulin.taskmanager.feature.home.presentation.state

import com.jacqulin.taskmanager.core.model.Tag

data class TaskState(
    val title: String = "",
    val tags: List<Tag> = emptyList(),
    val isPlannedForToday: Boolean = false,
)