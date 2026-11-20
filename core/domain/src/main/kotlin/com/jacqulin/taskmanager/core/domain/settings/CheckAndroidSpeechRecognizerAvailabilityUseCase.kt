package com.jacqulin.taskmanager.core.domain.settings

import com.jacqulin.taskmanager.core.data.system.SystemCapabilitiesChecker
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckAndroidSpeechRecognizerAvailabilityUseCase @Inject constructor(
    private val systemCapabilitiesChecker: SystemCapabilitiesChecker,
) {
    operator fun invoke(): Boolean =
        systemCapabilitiesChecker.isAndroidSpeechRecognizerAvailable()
}
