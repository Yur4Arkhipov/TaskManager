package com.jacqulin.taskmanager.feature.tasks.data.repository

import com.jacqulin.taskmanager.core.database.dao.TaskDao
import com.jacqulin.taskmanager.feature.tasks.data.mapper.toDomain
import com.jacqulin.taskmanager.feature.tasks.data.mapper.toEntity
import com.jacqulin.taskmanager.feature.tasks.domain.model.Task
import com.jacqulin.taskmanager.feature.tasks.domain.repository.TasksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TasksRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TasksRepository {
    override fun observeTasks(): Flow<List<Task>> {
        return taskDao.observeTasks()
            .map { notes ->
                notes.map { it.toDomain() }
            }
    }

    override suspend fun createTask(task: Task) {
        return taskDao.insertTask(task.toEntity())
    }

    override suspend fun updateTask(task: Task) {
        return taskDao.updateTask(task.toEntity())
    }

    override suspend fun deleteTaskById(taskId: Int) {
        return taskDao.deleteTaskById(taskId)
    }
}
