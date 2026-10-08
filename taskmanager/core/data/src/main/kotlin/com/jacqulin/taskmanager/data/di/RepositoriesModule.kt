package com.jacqulin.taskmanager.data.di

import com.jacqulin.taskmanager.data.repository.NoteImageRepository
import com.jacqulin.taskmanager.data.repository.NoteImageRepositoryImpl
import com.jacqulin.taskmanager.data.repository.NotesRepository
import com.jacqulin.taskmanager.data.repository.NotesRepositoryImpl
import com.jacqulin.taskmanager.data.repository.UserDataRepository
import com.jacqulin.taskmanager.data.repository.UserDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {

    @Binds
    internal abstract fun bindsUserDataRepository(
        userDataRepository: UserDataRepositoryImpl,
    ): UserDataRepository

    @Binds
    @Singleton
    internal abstract fun bindNotesRepository(
        notesRepository: NotesRepositoryImpl
    ): NotesRepository

    @Binds
    @Singleton
    internal abstract fun bindNoteImageRepository(
        noteImageRepository: NoteImageRepositoryImpl
    ): NoteImageRepository
}
