package com.jacqulin.taskmanager.core.platform.di

import com.jacqulin.taskmanager.core.platform.language.AppLanguageManager
import com.jacqulin.taskmanager.core.platform.language.AppLanguageManagerImpl
import com.jacqulin.taskmanager.core.platform.speech.SystemSpeechRecognizer
import com.jacqulin.taskmanager.core.platform.speech.SystemSpeechRecognizerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PlatformModule {

    @Binds
    @Singleton
    internal abstract fun bindAppLanguageManager(
        implementation: AppLanguageManagerImpl
    ): AppLanguageManager

    @Binds
    internal abstract fun bindsSystemSpeechRecognizer(
        impl: SystemSpeechRecognizerImpl,
    ): SystemSpeechRecognizer
}
