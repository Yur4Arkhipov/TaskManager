package com.jacqulin.taskmanager.feature.home.presentation

import com.jacqulin.taskmanager.feature.home.presentation.model.HomeTaskUi

data class HomeUiState(
    val dateLabel: String = "",
    val tasks: List<HomeTaskUi> = emptyList(),
    val isFocusStarted: Boolean = false,
    val isFullPlanShown: Boolean = false,
) {
    val totalMinutes: Int
        get() = tasks.sumOf { it.durationMinutes ?: 0 }

    val progress: Float
        get() = if (tasks.isEmpty()) 0f else (tasks.size / 9f).coerceAtMost(1f)
}
