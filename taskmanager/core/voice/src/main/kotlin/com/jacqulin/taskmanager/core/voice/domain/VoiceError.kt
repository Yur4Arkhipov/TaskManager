package com.jacqulin.taskmanager.core.voice.domain

import retrofit2.HttpException
import java.io.IOException

sealed interface VoiceError {
    data object RecordingFailed : VoiceError
    data object RecognitionFailed : VoiceError
    data object Network : VoiceError
    data object Unauthorized : VoiceError
    data object EmptyResult : VoiceError
    data object Unknown : VoiceError
}

sealed class SpeechException(message: String) : Exception(message) {
    class NotConfigured : SpeechException("Speech settings are not configured")
    class ProviderNotImplemented(providerId: String) :
        SpeechException("Speech provider not implemented: $providerId")
    class EmptyResult : SpeechException("Speech provider returned an empty result")
    class Unauthorized : SpeechException("Speech provider rejected credentials")
}

object VoiceErrorMapper {

    fun map(throwable: Throwable): VoiceError = when (throwable) {
        is SpeechException.NotConfigured -> VoiceError.RecognitionFailed
        is SpeechException.ProviderNotImplemented -> VoiceError.RecognitionFailed
        is SpeechException.Unauthorized -> VoiceError.Unauthorized
        is SpeechException.EmptyResult -> VoiceError.EmptyResult
        is HttpException -> when (throwable.code()) {
            401, 403 -> VoiceError.Unauthorized
            else -> VoiceError.RecognitionFailed
        }
        is IOException -> VoiceError.Network
        else -> VoiceError.Unknown
    }
}