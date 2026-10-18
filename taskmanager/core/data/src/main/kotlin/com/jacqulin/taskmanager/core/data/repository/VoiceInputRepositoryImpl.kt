package com.jacqulin.taskmanager.core.data.repository

import android.util.Log
import com.jacqulin.taskmanager.core.model.VoiceInputMode
import com.jacqulin.taskmanager.core.model.voice.SpeechRecognitionEvent
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.milliseconds

/**
 * Selects a voice strategy once per session, so settings changes apply to the next session.
 */
@Singleton
internal class VoiceInputRepositoryImpl @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val liveSpeechRecognitionRepository: LiveSpeechRecognitionRepository,
    private val audioRecorderRepository: AudioRecorderRepository,
    private val speechRecognitionRepository: SpeechRecognitionRepository,
) : VoiceInputRepository {
    private data class Session(
        val mode: VoiceInputMode,
        val liveResult: CompletableDeferred<String>? = null,
    )

    private val sessionMutex = Mutex()
    private var activeSession: Session? = null

    private fun log(message: String) {
        Log.d(TAG, "${System.currentTimeMillis()} [${Thread.currentThread().name}] $message")
    }

    override fun listen(): Flow<SpeechRecognitionEvent> = flow {
        log("listen(): collector started")
        val session = sessionMutex.withLock { activeSession }
        val mode = session?.mode ?: userDataRepository.userData.first().voiceInputMode
        val sessionResult = session?.liveResult
        log("listen(): using mode=$mode")
        try {
            if (mode == VoiceInputMode.LIVE) {
                log("listen(): collecting live recognizer flow")
                liveSpeechRecognitionRepository.listen().collect { event ->
                    log("listen(): received event=$event")
                    when (event) {
                        is SpeechRecognitionEvent.Result -> {
                            log("listen(): completing deferred with final result")
                            sessionResult?.complete(event.text)
                        }
                        is SpeechRecognitionEvent.Error -> {
                            log("listen(): completing deferred exceptionally")
                            sessionResult?.completeExceptionally(
                                IllegalStateException("Speech recognition failed: ${event.reason}"),
                            )
                        }
                        else -> Unit
                    }
                    emit(event)
                }
            }
        } finally {
            if (mode == VoiceInputMode.LIVE && session != null) {
                log("listen(): live flow ended; clearing completed/cancelled session state")
                sessionResult?.cancel()
                clearSession(session)
            }
        }
        log("listen(): collector completed")
    }

    override suspend fun start(): Result<Unit> = try {
        log("start(): reading current voice input setting")
        val mode = userDataRepository.userData.first().voiceInputMode
        log("start(): session mode=$mode")
        if (mode != VoiceInputMode.LIVE) {
            log("start(): starting audio recorder")
            audioRecorderRepository.start()
            log("start(): audio recorder started")
        }
        val session = if (mode == VoiceInputMode.LIVE) {
            Session(mode = mode, liveResult = CompletableDeferred()).also {
                log("start(): created liveResult deferred; it is waiting for recognition")
            }
        } else {
            Session(mode = mode)
        }
        sessionMutex.withLock {
            activeSession = session
        }
        Result.success(Unit)
    } catch (exception: CancellationException) {
        log("start(): cancelled: ${exception.message}")
        throw exception
    } catch (exception: Exception) {
        Log.e(TAG, "${System.currentTimeMillis()} [${Thread.currentThread().name}] start(): failed", exception)
        Result.failure(exception)
    }

    override suspend fun stop(): Result<String> {
        val session = sessionMutex.withLock { activeSession }
        val mode = session?.mode ?: userDataRepository.userData.first().voiceInputMode
        log("stop(): stopping session mode=$mode")
        return try {
            when (mode) {
                VoiceInputMode.LIVE -> {
                    val resultDeferred = session?.liveResult
                    log("stop(): asking live recognizer to stop")
                    liveSpeechRecognitionRepository.stop()
                    log("stop(): waiting up to 5 seconds for liveResult; coroutine suspends here")
                    val text = withTimeout(5_000.milliseconds) {
                        resultDeferred?.await() ?: error("No live session")
                    }
                    log("stop(): liveResult completed; coroutine resumed with ${text.length} chars")
                    Result.success(text)
                }
                else -> {
                    log("stop(): stopping audio recorder")
                    val audio = audioRecorderRepository.stop()
                    log("stop(): got ${audio.size} audio bytes; requesting recognition")
                    speechRecognitionRepository.recognize(audio).mapCatching { text ->
                        log("stop(): recognition returned ${text.length} chars")
                        text.takeIf(String::isNotBlank)
                            ?: throw IllegalStateException("Empty recognition result")
                    }
                }
            }
        } catch (exception: TimeoutCancellationException) {
            Log.w(TAG, "${System.currentTimeMillis()} [${Thread.currentThread().name}] stop(): timed out waiting for live result after 5 seconds", exception)
            Result.failure(exception)
        } catch (exception: CancellationException) {
            log("stop(): coroutine cancelled or timed out: ${exception.message}")
            throw exception
        } catch (exception: Exception) {
            Log.e(TAG, "${System.currentTimeMillis()} [${Thread.currentThread().name}] stop(): failed", exception)
            Result.failure(exception)
        } finally {
            log("stop(): clearing session state")
            session?.let { clearSession(it) }
        }
    }

    override suspend fun cancel() {
        val session = sessionMutex.withLock {
            activeSession.also { activeSession = null }
        }
        log("cancel(): cancelling mode=${session?.mode}")
        when (session?.mode) {
            VoiceInputMode.LIVE -> liveSpeechRecognitionRepository.stop()
            null -> Unit
            else -> audioRecorderRepository.cancel()
        }
        session?.liveResult?.cancel()
        log("cancel(): session state cleared")
    }

    private suspend fun clearSession(session: Session) {
        sessionMutex.withLock {
            if (activeSession === session) {
                activeSession = null
            }
        }
    }

    private companion object {
        const val TAG = "VoiceInputSession"
    }
}
