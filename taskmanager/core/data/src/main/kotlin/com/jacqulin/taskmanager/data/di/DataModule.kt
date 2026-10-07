package com.jacqulin.taskmanager.data.di

import com.jacqulin.taskmanager.data.domain.AppSettingsRepository
import com.jacqulin.taskmanager.data.domain.VoiceSettingsRepository
import com.jacqulin.taskmanager.data.repository.AppSettingsRepositoryImpl
import com.jacqulin.taskmanager.data.repository.VoiceSettingsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindAppSettingsRepository(
        impl: AppSettingsRepositoryImpl,
    ): AppSettingsRepository

    @Binds
    abstract fun bindVoiceSettingsRepository(
        impl: VoiceSettingsRepositoryImpl,
    ): VoiceSettingsRepository
}
