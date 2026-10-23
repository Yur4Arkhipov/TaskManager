package com.jacqulin.taskmanager.core.database.di

import com.jacqulin.taskmanager.core.database.TaskManagerDatabase
import com.jacqulin.taskmanager.core.database.dao.NoteDao
import com.jacqulin.taskmanager.core.database.dao.TagsDao
import com.jacqulin.taskmanager.core.database.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {

    @Provides
    fun provideNoteDao(
        database: TaskManagerDatabase,
    ): NoteDao = database.noteDao()

    @Provides
    fun provideTaskDao(
        database: TaskManagerDatabase,
    ): TaskDao = database.taskDao()

    @Provides
    fun provideTagsDao(
        database: TaskManagerDatabase
    ): TagsDao = database.tagsDao()
}
