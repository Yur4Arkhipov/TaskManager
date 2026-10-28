package com.jacqulin.taskmanager.feature.home.presentation.model

data class HomeUiState(
    val mainState: MainState = MainState(),
    val taskState: TaskState = TaskState(),
//    val availableTags: List<Tag> = emptyList(),
) {
    val totalMinutes: Int
        get() = mainState.tasks.sumOf { it.durationMinutes ?: 0 }

    val progress: Float
        get() = if (mainState.tasks.isEmpty()) {
            0f
        } else {
            mainState.tasks.count { it.isCompleted }.toFloat() / mainState.tasks.size
        }

    val isEmpty: Boolean
        get() = mainState.tasks.isEmpty()
}