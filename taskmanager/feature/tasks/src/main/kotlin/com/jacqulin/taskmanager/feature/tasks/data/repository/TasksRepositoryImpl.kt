package com.jacqulin.taskmanager.feature.tasks.data.repository

import com.jacqulin.taskmanager.feature.tasks.domain.model.Task
import com.jacqulin.taskmanager.feature.tasks.domain.repository.TasksRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class TasksRepositoryImpl @Inject constructor(

) : TasksRepository {
    override fun observeTasks(): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskById(id: Int): Task? {
        TODO("Not yet implemented")
    }

    override suspend fun createTask(note: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(note: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTaskById(noteId: Int) {
        TODO("Not yet implemented")
    }
}
