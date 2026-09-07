package com.jacqulin.taskmanager.feature.tasks.domain.repository

import com.jacqulin.taskmanager.feature.tasks.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    fun observeTasks(): Flow<List<Task>>
    suspend fun createTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTaskById(taskId: Int)
}