package com.jacqulin.taskmanager.core.storage.audio

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

internal class AudioRecorderImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : AudioRecorder {

    private var recorder: MediaRecorder? = null
    private var outputFile: File? = null

    override suspend fun start() = withContext(Dispatchers.IO) {
        Log.d("AudioRecorder", "[start] ${Thread.currentThread().name}")
        if (recorder != null) return@withContext // already recording — ignore duplicate start

        val file = File.createTempFile("voice_", ".ogg", context.cacheDir)
        outputFile = file

        try {
            recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.OGG)
                setOutputFile(file.absolutePath)
                setAudioEncoder(MediaRecorder.AudioEncoder.OPUS)

                prepare() // initialize codecs and open the output file
                start()
            }
        } catch (e: Exception) {
            recorder?.release() // free native resources on failure
            recorder = null
            outputFile?.delete()
            outputFile = null
            throw e
        }
    }

    override suspend fun stop(): ByteArray = withContext(Dispatchers.IO) {
        Log.d("AudioRecorder", "[stop] ${Thread.currentThread().name}")

        val currentRecorder = checkNotNull(recorder) { "Recorder is not started" }
        val file = checkNotNull(outputFile) { "Output file is missing" }

        recorder = null
        outputFile = null

        try {
            currentRecorder.stop()
            file.readBytes()
        } finally {
            currentRecorder.release()
            file.delete()
        }
    }

    override suspend fun cancel() = withContext(Dispatchers.IO) {
        Log.d("AudioRecorder", "[cancel] ${Thread.currentThread().name}")
        recorder?.let {
            try {
                it.stop()
            } catch (_: RuntimeException) {
                Log.e("AudioRecorder", "[cancel] error when stoping")
            }

            it.release()
        }

        recorder = null
        outputFile?.delete()
        outputFile = null
    }
}
