package com.jacqulin.taskmanager.feature.notes.di

import com.jacqulin.taskmanager.feature.notes.data.repository.NotesRepositoryImpl
import com.jacqulin.taskmanager.feature.notes.domain.repository.NotesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotesModule {

    @Binds
    @Singleton
    abstract fun bindNotesRepository(
        impl: NotesRepositoryImpl
    ): NotesRepository
}
