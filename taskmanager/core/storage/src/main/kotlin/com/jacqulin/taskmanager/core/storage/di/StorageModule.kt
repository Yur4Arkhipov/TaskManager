package com.jacqulin.taskmanager.core.storage.di

import com.jacqulin.taskmanager.core.storage.NoteImageStorage
import com.jacqulin.taskmanager.core.storage.NoteImageStorageImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {

    @Binds
    internal abstract fun bindsNoteImageStorage(
        noteImageStorage: NoteImageStorageImpl,
    ): NoteImageStorage
}
