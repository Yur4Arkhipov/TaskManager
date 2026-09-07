package com.jacqulin.taskmanager.core.database.di

import android.content.Context
import androidx.room.Room
import com.jacqulin.taskmanager.core.database.TaskManagerDatabase
import com.jacqulin.taskmanager.core.database.dao.NoteDao
import com.jacqulin.taskmanager.core.database.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): TaskManagerDatabase {
        return Room.databaseBuilder(
            context,
            TaskManagerDatabase::class.java,
            "task_manager.db",
        ).build()
    }

    @Provides
    fun provideNoteDao(
        database: TaskManagerDatabase,
    ): NoteDao {
        return database.noteDao()
    }

    @Provides
    fun provideTaskDao(
        database: TaskManagerDatabase,
    ): TaskDao {
        return database.taskDao()
    }
}
