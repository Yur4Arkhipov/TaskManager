package com.jacqulin.taskmanager.core.storage.di

import com.jacqulin.taskmanager.core.storage.audio.AudioRecorder
import com.jacqulin.taskmanager.core.storage.audio.AudioRecorderImpl
import com.jacqulin.taskmanager.core.storage.image.NoteImageStorage
import com.jacqulin.taskmanager.core.storage.image.NoteImageStorageImpl
import com.jacqulin.taskmanager.core.storage.security.AndroidKeystoreTokenStorage
import com.jacqulin.taskmanager.core.storage.security.SecureTokenStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {

    @Binds
    internal abstract fun bindsNoteImageStorage(
        noteImageStorage: NoteImageStorageImpl,
    ): NoteImageStorage

    @Binds
    internal abstract fun bindsAudioRecorder(
        audioRecorder: AudioRecorderImpl,
    ): AudioRecorder

    @Binds
    @Singleton
    internal abstract fun bindSecureTokenStore(
        impl: AndroidKeystoreTokenStorage
    ): SecureTokenStorage
}
