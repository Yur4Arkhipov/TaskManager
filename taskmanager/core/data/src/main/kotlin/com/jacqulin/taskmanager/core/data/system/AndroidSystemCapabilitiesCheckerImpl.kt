package com.jacqulin.taskmanager.core.data.system

import com.jacqulin.taskmanager.core.speech.SystemSpeechRecognizer
import javax.inject.Inject

internal class AndroidSystemCapabilitiesCheckerImpl @Inject constructor(
    private val systemSpeechRecognizer: SystemSpeechRecognizer,
) : SystemCapabilitiesChecker {
    override fun isSpeechRecognizerAvailable(): Boolean =
        systemSpeechRecognizer.isAvailable
}
