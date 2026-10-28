package com.jacqulin.taskmanager.feature.home.presentation.focus

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.jacqulin.taskmanager.core.model.FocusMode
import com.jacqulin.taskmanager.feature.home.presentation.state.FocusSessionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FocusSessionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val mode: FocusMode = checkNotNull(savedStateHandle.get<FocusMode>("mode"))

    private val _uiState = MutableStateFlow(
        FocusSessionUiState(
            mode = mode,
            durationMinutes = when (mode) {
                FocusMode.POMODORO -> 25
                FocusMode.TIMER -> null
                FocusMode.FREE -> null
            }
        )
    )
    val uiState: StateFlow<FocusSessionUiState> = _uiState.asStateFlow()

}