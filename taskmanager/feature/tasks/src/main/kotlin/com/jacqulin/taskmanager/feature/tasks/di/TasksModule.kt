package com.jacqulin.taskmanager.feature.tasks.di

import com.jacqulin.taskmanager.feature.tasks.data.repository.TasksRepositoryImpl
import com.jacqulin.taskmanager.feature.tasks.domain.repository.TasksRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TasksModule {

    @Binds
    @Singleton
    abstract fun bindTasksRepository(
        impl: TasksRepositoryImpl
    ): TasksRepository
}