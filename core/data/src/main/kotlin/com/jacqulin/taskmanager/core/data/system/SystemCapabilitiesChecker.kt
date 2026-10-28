package com.jacqulin.taskmanager.core.data.system

interface SystemCapabilitiesChecker {
    fun isSpeechRecognizerAvailable(): Boolean
}
