package com.jacqulin.taskmanager.feature.home.presentation

import com.jacqulin.taskmanager.feature.home.presentation.state.MainState
import com.jacqulin.taskmanager.feature.home.presentation.state.TaskState

data class HomeUiState(
    val mainState: MainState = MainState(),
    val taskState: TaskState = TaskState(),
//    val tasks: List<HomeTaskUi> = emptyList(),
//    val availableTags: List<Tag> = emptyList(),
) {
    val totalMinutes: Int
        get() = mainState.tasks.sumOf { it.durationMinutes ?: 0 }

    val progress: Float
        get() = if (mainState.tasks.isEmpty()) 0f else (mainState.tasks.size / 9f).coerceAtMost(1f)

    val isEmpty: Boolean
        get() = mainState.tasks.isEmpty()
}
