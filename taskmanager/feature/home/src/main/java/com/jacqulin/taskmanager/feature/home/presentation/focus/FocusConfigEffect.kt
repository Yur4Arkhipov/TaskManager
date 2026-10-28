package com.jacqulin.taskmanager.feature.home.presentation.focus

import com.jacqulin.taskmanager.core.model.FocusMode

sealed interface FocusConfigEffect {
    data class StartFocus(
        val taskId: Int,
        val durationMinutes: Int,
        val mode: FocusMode,
    ) : FocusConfigEffect
}
