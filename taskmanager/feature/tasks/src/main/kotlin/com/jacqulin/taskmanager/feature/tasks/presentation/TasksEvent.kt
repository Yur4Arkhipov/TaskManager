package com.jacqulin.taskmanager.feature.tasks.presentation

import com.jacqulin.taskmanager.core.designsystem.model.SortType
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TaskFilter
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TaskItemUi

sealed interface TasksEvent {
    data class OnSearchQueryChanged(val query: String) : TasksEvent
    data class OnSortChanged(val sortType: SortType) : TasksEvent
    data class OnTaskFilterChanged(val filter: TaskFilter) : TasksEvent
    data class OnDeleteTaskClicked(val taskId: Int) : TasksEvent
    data object OnSaveTaskClicked : TasksEvent
    data object OnCreateTaskByTextClicked : TasksEvent
    data object OnBottomSheetDismissed : TasksEvent
    data class OnTaskTextChanged(val title: String) : TasksEvent
    data class OnTaskTimeEstimateSelected(val estimate: String) : TasksEvent
    data class OnTaskPlanForTodayChanged(val enabled: Boolean) : TasksEvent
    data object OnTaskAddTagClicked : TasksEvent
    data class UpdateTaskStatus(val task: TaskItemUi) : TasksEvent
    data object OnCreateTaskByVoiceClicked : TasksEvent
    data object VoiceInputStopClicked : TasksEvent
    data object VoicePermissionGranted : TasksEvent
    data object VoicePermissionDenied : TasksEvent
    data object VoiceInputRetry : TasksEvent
    data object VoiceInputCancel : TasksEvent
}
