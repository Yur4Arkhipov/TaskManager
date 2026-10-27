package com.jacqulin.taskmanager.feature.home.presentation.focus

import com.jacqulin.taskmanager.feature.home.presentation.model.FocusMode

sealed interface FocusConfigEffect {
    data class StartFocus(
        val taskId: Int,
        val durationMinutes: Int,
        val mode: FocusMode,
    ) : FocusConfigEffect
}
