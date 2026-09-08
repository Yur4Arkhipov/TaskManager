package com.jacqulin.taskmanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacqulin.taskmanager.data.domain.AppSettingsRepository
import com.jacqulin.taskmanager.ui.MainActivityUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    appSettingsRepository: AppSettingsRepository,
) : ViewModel() {
    val uiState = appSettingsRepository.appSettings.map { settings ->
        MainActivityUiState.Success(settings)
    }.stateIn(
        scope = viewModelScope,
        initialValue = MainActivityUiState.Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )
}
