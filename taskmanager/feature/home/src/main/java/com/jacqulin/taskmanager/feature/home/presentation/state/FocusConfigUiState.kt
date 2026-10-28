package com.jacqulin.taskmanager.feature.home.presentation.state

import com.jacqulin.taskmanager.feature.home.presentation.model.FocusMode

data class FocusConfigUiState(
    val isLoading: Boolean = true,
    val taskTitle: String = "",

    val pomodoroDurationMinutes: Int = 25,
    val pomodoroRelaxMinutes: Int = 5,

    val timerDurationMinutes: Int = 25,

    val focusMode: FocusMode = FocusMode.POMODORO,
    val errorMessage: String? = null,
    val isStarting: Boolean = false,
)
