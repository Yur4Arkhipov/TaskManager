package com.jacqulin.taskmanager.feature.tasks.presentation

import com.jacqulin.taskmanager.core.designsystem.model.SortType
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TaskFilter
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TaskItemUi
import com.jacqulin.taskmanager.core.model.Tag

sealed interface TasksEvent {
    data class OnSearchQueryChanged(val query: String) : TasksEvent
    data class OnSortChanged(val sortType: SortType) : TasksEvent
    data class OnTaskFilterChanged(val filter: TaskFilter) : TasksEvent
    data class OnDeleteTaskClicked(val taskId: Int) : TasksEvent
    data object OnSaveTaskClicked : TasksEvent
    data object OnDeleteModeToggled : TasksEvent
    data class OnTaskSelectionToggled(val taskId: Int) : TasksEvent
    data class OnEnterDeleteMode(val taskId: Int) : TasksEvent
    data object OnSelectAllTasks : TasksEvent
    data object OnDeleteSelectedTasks : TasksEvent
    data class OnShowCreationDateChanged(val enabled: Boolean) : TasksEvent
    data class OnShowTagsChanged(val enabled: Boolean) : TasksEvent
    data class OnCompactViewChanged(val enabled: Boolean) : TasksEvent
    data object OnCreateTaskByTextClicked : TasksEvent
    data object OnBottomSheetDismissed : TasksEvent
    data class OnTaskTextChanged(val title: String) : TasksEvent
    data class OnTaskTimeEstimateSelected(val estimate: String) : TasksEvent
    data class OnTaskPlanForTodayChanged(val enabled: Boolean) : TasksEvent
    data class OnTaskTagAdded(val tag: Tag) : TasksEvent
    data class OnTaskTagRemoved(val tag: Tag) : TasksEvent
    data class UpdateTaskStatus(val task: TaskItemUi) : TasksEvent
    data object OnCreateTaskByVoiceClicked : TasksEvent
    data object VoiceInputStopClicked : TasksEvent
    data object VoicePermissionGranted : TasksEvent
    data object VoicePermissionDenied : TasksEvent
    data object VoiceInputRetry : TasksEvent
    data object VoiceInputCancel : TasksEvent
}
