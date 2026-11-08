package com.jacqulin.taskmanager.core.platform.di

import com.jacqulin.taskmanager.core.platform.speech.SystemSpeechRecognizer
import com.jacqulin.taskmanager.core.platform.speech.SystemSpeechRecognizerImpl
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
