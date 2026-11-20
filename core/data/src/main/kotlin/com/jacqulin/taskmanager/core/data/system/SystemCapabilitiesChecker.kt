package com.jacqulin.taskmanager.core.data.system

interface SystemCapabilitiesChecker {
    fun isAndroidSpeechRecognizerAvailable(): Boolean
    fun isOggOpusRecordingAvailable(): Boolean
}
