package com.jacqulin.taskmanager.core.data.system

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import com.jacqulin.taskmanager.core.platform.speech.SystemSpeechRecognizer
import javax.inject.Inject

internal class AndroidSystemCapabilitiesCheckerImpl @Inject constructor(
    private val systemSpeechRecognizer: SystemSpeechRecognizer,
) : SystemCapabilitiesChecker {
    override fun isAndroidSpeechRecognizerAvailable(): Boolean =
        systemSpeechRecognizer.isAvailable

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
    override fun isOggOpusRecordingAvailable(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }
}
