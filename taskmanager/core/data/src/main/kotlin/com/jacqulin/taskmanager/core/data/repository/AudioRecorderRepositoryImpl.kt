package com.jacqulin.taskmanager.core.data.repository

import com.jacqulin.taskmanager.core.storage.audio.AudioRecorder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AudioRecorderRepositoryImpl @Inject constructor(
    private val audioRecorder: AudioRecorder,
) : AudioRecorderRepository {

    override fun start() = audioRecorder.start()
    override suspend fun stop(): ByteArray = audioRecorder.stop()
    override fun cancel() = audioRecorder.cancel()
}
