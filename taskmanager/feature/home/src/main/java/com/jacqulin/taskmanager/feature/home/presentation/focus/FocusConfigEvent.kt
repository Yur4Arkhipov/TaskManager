package com.jacqulin.taskmanager.feature.home.presentation.focus

import com.jacqulin.taskmanager.core.model.FocusMode

sealed interface FocusConfigEvent {
    data class ModeChanged(val mode: FocusMode) : FocusConfigEvent
    data object StartFocusClicked : FocusConfigEvent

    data class PomodoroDurationChanged(val minutes: Int) : FocusConfigEvent
    data class TimerDurationChanged(val minutes: Int) : FocusConfigEvent
}
