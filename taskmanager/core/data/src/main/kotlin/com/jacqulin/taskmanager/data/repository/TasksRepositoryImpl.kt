package com.jacqulin.taskmanager.data.repository

import com.jacqulin.taskmanager.core.database.dao.TaskDao
import com.jacqulin.taskmanager.core.database.entity.asExternalModel
import com.jacqulin.taskmanager.core.database.entity.toEntity
import com.jacqulin.taskmanager.core.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TasksRepositoryImpl @Inject constructor(
    private val dao: TaskDao
) : TasksRepository {
    override fun observeTasks(): Flow<List<Task>> {
        return dao.observeTasks()
            .map { notes ->
                notes.map { it.asExternalModel() }
            }
    }

    override suspend fun createTask(task: Task) {
        return dao.insertTask(task.toEntity())
    }

    override suspend fun updateTask(task: Task) {
        return dao.updateTask(task.toEntity())
    }

    override suspend fun deleteTaskById(taskId: Int) {
        return dao.deleteTaskById(taskId)
    }
}
