package com.jacqulin.taskmanager.feature.home.presentation.state

import com.jacqulin.taskmanager.feature.home.presentation.model.FocusMode

data class FocusSessionUiState(
    val mode: FocusMode = FocusMode.FREE,
    val taskTitle: String = "",
    val durationMinutes: Int? = null,
    val isRunning: Boolean = false,
)
