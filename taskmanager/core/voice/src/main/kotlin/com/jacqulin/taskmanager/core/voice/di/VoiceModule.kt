package com.jacqulin.taskmanager.core.voice.di

import com.jacqulin.taskmanager.core.voice.data.VoiceRecognizerImpl
import com.jacqulin.taskmanager.core.voice.data.audio.AndroidAudioRecorder
import com.jacqulin.taskmanager.core.voice.data.speech.SpeechToTextImpl
import com.jacqulin.taskmanager.core.voice.domain.AudioRecorder
import com.jacqulin.taskmanager.core.voice.domain.SpeechToText
import com.jacqulin.taskmanager.core.voice.domain.VoiceRecognizer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class VoiceModule {

    @Binds
    @Singleton
    abstract fun bindAudioRecorder(
        impl: AndroidAudioRecorder,
    ): AudioRecorder

    @Binds
    @Singleton
    abstract fun bindSpeechToText(
        impl: SpeechToTextImpl,
    ): SpeechToText

    @Binds
    @Singleton
    abstract fun bindVoiceRecognizer(
        impl: VoiceRecognizerImpl,
    ): VoiceRecognizer
}
