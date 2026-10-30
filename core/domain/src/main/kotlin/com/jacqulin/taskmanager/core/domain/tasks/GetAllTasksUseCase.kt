package com.jacqulin.taskmanager.core.domain.tasks

import com.jacqulin.taskmanager.core.data.repository.TasksRepository
import com.jacqulin.taskmanager.core.model.Task
import javax.inject.Inject

class GetAllTasksUseCase @Inject constructor(
    private val repository: TasksRepository
) {
    suspend operator fun invoke(): List<Task> = repository.getAllTasks()
}
