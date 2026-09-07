package com.jacqulin.taskmanager.core.voice.data.audio

import android.content.Context
import android.media.MediaRecorder
import com.jacqulin.taskmanager.core.voice.domain.AudioRecorder
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class AndroidAudioRecorder @Inject constructor(
    @ApplicationContext private val context: Context,
) : AudioRecorder {

    private var recorder: MediaRecorder? = null
    private var outputFile: File? = null

    override fun start() {
        if (recorder != null) return

        val file = File.createTempFile(
            "voice_",
            ".ogg",
            context.cacheDir,
        )

        outputFile = file

        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.OGG)
            setAudioEncoder(MediaRecorder.AudioEncoder.OPUS)
            setOutputFile(file.absolutePath)

            prepare()
            start()
        }
    }

    override suspend fun stop(): ByteArray {
        val currentRecorder = recorder
            ?: throw IllegalStateException("Recorder is not started")

        val file = outputFile
            ?: throw IllegalStateException("Output file is missing")

        try {
            currentRecorder.stop()
        } finally {
            currentRecorder.release()
            recorder = null
        }

        return try {
            file.readBytes()
        } finally {
            file.delete()
            outputFile = null
        }
    }

    override fun cancel() {
        recorder?.let {
            try {
                it.stop()
            } catch (_: RuntimeException) {
                // Recording could have been too short.
            }

            it.release()
        }

        recorder = null

        outputFile?.delete()
        outputFile = null
    }
}