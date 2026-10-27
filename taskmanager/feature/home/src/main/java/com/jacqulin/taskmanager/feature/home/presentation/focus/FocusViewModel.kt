package com.jacqulin.taskmanager.feature.home.presentation.focus

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacqulin.taskmanager.core.domain.tasks.GetTaskByIdUseCase
import com.jacqulin.taskmanager.feature.home.presentation.model.FocusMode
import com.jacqulin.taskmanager.feature.home.presentation.state.FocusConfigUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FocusViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTaskUseCase: GetTaskByIdUseCase,
) : ViewModel() {

    private val taskId: Int = checkNotNull(savedStateHandle.get<Int>("taskId"))

    private val _uiState = MutableStateFlow(FocusConfigUiState())
    val uiState: StateFlow<FocusConfigUiState> =
        _uiState.asStateFlow()

    private val _effects = Channel<FocusConfigEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        loadTask()
    }

    fun onEvent(event: FocusConfigEvent) {
        when (event) {
            is FocusConfigEvent.DurationChanged -> {
                updateDuration(event.minutes)
            }

            is FocusConfigEvent.ModeChanged -> {
                updateMode(event.mode)
            }

            FocusConfigEvent.StartFocusClicked -> {
                startFocus()
            }
        }
    }

    private fun loadTask() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            runCatching {
                getTaskUseCase(taskId)
            }.onSuccess { task ->
                if (task == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Задача не найдена"
                        )
                    }
                    return@onSuccess
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        taskTitle = task.title,
                    )
                }
            }.onFailure {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Не удалось загрузить задачу"
                    )
                }
            }
        }
    }

    private fun updateDuration(minutes: Int) {
        if (minutes <= 0) return

        _uiState.update {
            it.copy(durationMinutes = minutes)
        }
    }

    private fun updateMode(mode: FocusMode) {
        _uiState.update {
            it.copy(focusMode = mode)
        }
    }

    private fun startFocus() {
        val state = _uiState.value

        if (state.isLoading || state.isStarting) return
        if (state.errorMessage != null || state.taskTitle.isBlank()) return

        viewModelScope.launch {
            _effects.send(
                FocusConfigEffect.StartFocus(
                    taskId = taskId,
                    durationMinutes = state.durationMinutes,
                    mode = state.focusMode,
                )
            )
        }
    }
}
