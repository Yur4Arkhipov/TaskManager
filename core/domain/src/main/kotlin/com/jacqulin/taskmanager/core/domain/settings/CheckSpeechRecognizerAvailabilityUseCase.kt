package com.jacqulin.taskmanager.core.domain.settings

import com.jacqulin.taskmanager.core.data.system.SystemCapabilitiesChecker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckSpeechRecognizerAvailabilityUseCase @Inject constructor(
    systemCapabilitiesChecker: SystemCapabilitiesChecker,
) {
    private val _isAvailable = MutableStateFlow(
        systemCapabilitiesChecker.isSpeechRecognizerAvailable()
    )
    val isAvailable: StateFlow<Boolean> = _isAvailable.asStateFlow()
}
