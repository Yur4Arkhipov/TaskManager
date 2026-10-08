package com.jacqulin.taskmanager.data.repository

import com.jacqulin.taskmanager.core.model.Task
import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    fun observeTasks(): Flow<List<Task>>
    suspend fun createTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTaskById(taskId: Int)
}
