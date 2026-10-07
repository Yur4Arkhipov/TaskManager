package com.jacqulin.taskmanager.feature.notes.domain.usecase

import com.jacqulin.taskmanager.core.voice.domain.VoiceErrorMapper
import com.jacqulin.taskmanager.core.voice.domain.VoiceState
import javax.inject.Inject

//class RecognizeVoiceUseCase @Inject constructor(
//    private val speechToText: SpeechToText,
//) {
//    suspend operator fun invoke(audio: ByteArray): VoiceState {
//        return speechToText.recognize(audio).fold(
//            onSuccess = { VoiceState.Success(it) },
//            onFailure = { VoiceState.Error(VoiceErrorMapper.map(it)) },
//        )
//    }
//}