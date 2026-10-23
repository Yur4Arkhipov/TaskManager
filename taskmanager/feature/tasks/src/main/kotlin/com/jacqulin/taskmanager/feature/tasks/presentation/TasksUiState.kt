package com.jacqulin.taskmanager.feature.tasks.presentation

import com.jacqulin.taskmanager.core.model.Tag
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TaskItemUi
import com.jacqulin.taskmanager.feature.tasks.presentation.state.MainScreenState
import com.jacqulin.taskmanager.feature.tasks.presentation.state.TaskState

data class TasksUiState(
    val mainScreenState: MainScreenState = MainScreenState(),
    val taskState: TaskState = TaskState(),
    val visibleTasks: List<TaskItemUi> = emptyList(),
    val voiceState: VoiceState = VoiceState.Idle,
) {
    val availableTags: List<Tag> get() = mainScreenState.availableTags
    val isEmpty: Boolean get() = visibleTasks.isEmpty()
}
