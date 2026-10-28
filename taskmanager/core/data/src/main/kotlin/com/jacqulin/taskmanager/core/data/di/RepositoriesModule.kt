package com.jacqulin.taskmanager.core.data.di

import com.jacqulin.taskmanager.core.data.repository.AudioRecorderRepository
import com.jacqulin.taskmanager.core.data.repository.AudioRecorderRepositoryImpl
import com.jacqulin.taskmanager.core.data.repository.LiveSpeechRecognitionRepository
import com.jacqulin.taskmanager.core.data.repository.LiveSpeechRecognitionRepositoryImpl
import com.jacqulin.taskmanager.core.data.repository.NoteImageRepository
import com.jacqulin.taskmanager.core.data.repository.NoteImageRepositoryImpl
import com.jacqulin.taskmanager.core.data.repository.NotesRepository
import com.jacqulin.taskmanager.core.data.repository.NotesRepositoryImpl
import com.jacqulin.taskmanager.core.data.repository.SpeechRecognitionRepository
import com.jacqulin.taskmanager.core.data.repository.SpeechRecognitionRepositoryImpl
import com.jacqulin.taskmanager.core.data.repository.TagsRepository
import com.jacqulin.taskmanager.core.data.repository.TagsRepositoryImpl
import com.jacqulin.taskmanager.core.data.repository.TasksRepository
import com.jacqulin.taskmanager.core.data.repository.TasksRepositoryImpl
import com.jacqulin.taskmanager.core.data.repository.UserDataRepository
import com.jacqulin.taskmanager.core.data.repository.UserDataRepositoryImpl
import com.jacqulin.taskmanager.core.data.repository.VoiceInputRepository
import com.jacqulin.taskmanager.core.data.repository.VoiceInputRepositoryImpl
import com.jacqulin.taskmanager.core.data.system.AndroidSystemCapabilitiesCheckerImpl
import com.jacqulin.taskmanager.core.data.system.SystemCapabilitiesChecker
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
    internal abstract fun bindVoiceInputRepository(
        impl: VoiceInputRepositoryImpl
    ): VoiceInputRepository

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

    @Binds
    internal abstract fun bindTasksRepository(
        tasksRepository: TasksRepositoryImpl
    ): TasksRepository

    @Binds
    @Singleton
    internal abstract fun bindsAudioRecorderRepository(
        impl: AudioRecorderRepositoryImpl,
    ): AudioRecorderRepository

    @Binds
    @Singleton
    internal abstract fun bindsSpeechRecognitionRepository(
        impl: SpeechRecognitionRepositoryImpl,
    ): SpeechRecognitionRepository

    @Binds
    @Singleton
    internal abstract fun bindLiveSpeechRecognitionRepository(
        impl: LiveSpeechRecognitionRepositoryImpl,
    ): LiveSpeechRecognitionRepository

    @Binds
    @Singleton
    internal abstract fun bindsSystemCapabilitiesChecker(
        impl: AndroidSystemCapabilitiesCheckerImpl,
    ): SystemCapabilitiesChecker

    @Binds
    @Singleton
    internal abstract fun bindTagRepository(
        impl: TagsRepositoryImpl,
    ): TagsRepository

}
