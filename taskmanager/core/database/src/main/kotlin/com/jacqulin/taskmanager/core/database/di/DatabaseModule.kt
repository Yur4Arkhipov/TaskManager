package com.jacqulin.taskmanager.core.database.di

import android.content.Context
import androidx.room.Room
import com.jacqulin.taskmanager.core.database.TaskManagerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): TaskManagerDatabase = Room.databaseBuilder(
        context,
        TaskManagerDatabase::class.java,
        "task_manager.db",
    )
        .fallbackToDestructiveMigration(true)
        .build()
}
