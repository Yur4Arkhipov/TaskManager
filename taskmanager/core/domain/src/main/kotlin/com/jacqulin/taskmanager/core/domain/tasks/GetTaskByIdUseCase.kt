package com.jacqulin.taskmanager.core.domain.tasks

import com.jacqulin.taskmanager.core.data.repository.TasksRepository
import javax.inject.Inject

class GetTaskByIdUseCase @Inject constructor(
    private val repository: TasksRepository
) {
    suspend operator fun invoke(taskId: Int) = repository.getTaskById(taskId)
}
