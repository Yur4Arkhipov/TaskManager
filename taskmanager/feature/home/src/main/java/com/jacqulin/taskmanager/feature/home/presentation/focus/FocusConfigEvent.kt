package com.jacqulin.taskmanager.feature.home.presentation.focus

import com.jacqulin.taskmanager.feature.home.presentation.model.FocusMode

sealed interface FocusConfigEvent {
    data class DurationChanged(val minutes: Int) : FocusConfigEvent

    data class ModeChanged(val mode: FocusMode) : FocusConfigEvent

    data object StartFocusClicked : FocusConfigEvent
}
