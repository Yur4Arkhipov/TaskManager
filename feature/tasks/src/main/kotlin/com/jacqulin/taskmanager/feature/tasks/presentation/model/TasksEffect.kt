package com.jacqulin.taskmanager.feature.tasks.presentation.model

import androidx.annotation.StringRes

sealed interface TasksEffect {
    data object RequestVoicePermission : TasksEffect
    data class ShowError(
        @StringRes val messageResId: Int
    ) : TasksEffect
}
