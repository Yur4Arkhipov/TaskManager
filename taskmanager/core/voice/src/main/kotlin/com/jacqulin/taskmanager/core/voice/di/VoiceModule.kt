package com.jacqulin.taskmanager.core.voice.di

import com.jacqulin.taskmanager.core.voice.data.VoiceRecognizerImpl
import com.jacqulin.taskmanager.core.voice.data.audio.AndroidAudioRecorder
import com.jacqulin.taskmanager.core.voice.data.speech.SpeechToTextImpl
import com.jacqulin.taskmanager.core.voice.data.speech.YandexSpeechProvider
import com.jacqulin.taskmanager.core.voice.domain.SpeechProvider
import com.jacqulin.taskmanager.core.voice.domain.VoiceRecognizer
import com.jacqulin.taskmanager.core.voice.domain.internal.AudioRecorder
import com.jacqulin.taskmanager.core.voice.domain.internal.SpeechToText
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class VoiceModule {

    @Binds
    @Singleton
    internal abstract fun bindAudioRecorder(
        impl: AndroidAudioRecorder,
    ): AudioRecorder

    @Binds
    @Singleton
    internal abstract fun bindVoiceRecognizer(
        impl: VoiceRecognizerImpl,
    ): VoiceRecognizer

    @Binds
    @IntoSet
    abstract fun bindYandexSpeechProvider(impl: YandexSpeechProvider): SpeechProvider

    @Binds
    @Singleton
    abstract fun bindSpeechToText(impl: SpeechToTextImpl): SpeechToText
}
