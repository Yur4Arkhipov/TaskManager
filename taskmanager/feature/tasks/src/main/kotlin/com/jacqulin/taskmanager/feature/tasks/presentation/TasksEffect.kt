package com.jacqulin.taskmanager.feature.tasks.presentation

sealed interface TasksEffect {
    data object RequestVoicePermission : TasksEffect
    data class ShowError(val message: String) : TasksEffect
}
