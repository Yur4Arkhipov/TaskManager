package com.jacqulin.taskmanager.feature.tasks.presentation.model

import com.jacqulin.taskmanager.core.model.Tag
import com.jacqulin.taskmanager.feature.tasks.presentation.VoiceState

data class TasksUiState(
    val mainScreenState: MainScreenState = MainScreenState(),
    val taskState: TaskState = TaskState(),
    val visibleTasks: List<TaskItemUi> = emptyList(),
    val voiceState: VoiceState = VoiceState.Idle,
    val availableTags: List<Tag> = emptyList(),
) {
    val isEmpty: Boolean get() = visibleTasks.isEmpty()
}
