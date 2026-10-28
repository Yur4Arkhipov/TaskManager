package com.jacqulin.taskmanager.core.speech.di

import com.jacqulin.taskmanager.core.speech.SystemSpeechRecognizer
import com.jacqulin.taskmanager.core.speech.SystemSpeechRecognizerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SpeechModule {

    @Binds
    internal abstract fun bindsSystemSpeechRecognizer(
        impl: SystemSpeechRecognizerImpl,
    ): SystemSpeechRecognizer
}
