package com.jacqulin.taskmanager.feature.tasks.domain.repository

import com.jacqulin.taskmanager.feature.tasks.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    fun observeTasks(): Flow<List<Task>>
    suspend fun getTaskById(id: Int): Task?
    suspend fun createTask(note: Task)
    suspend fun updateTask(note: Task)
    suspend fun deleteTaskById(noteId: Int)
}