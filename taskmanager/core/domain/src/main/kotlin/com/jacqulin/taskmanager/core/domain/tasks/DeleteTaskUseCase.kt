package com.jacqulin.taskmanager.core.domain.tasks

import com.jacqulin.taskmanager.data.repository.TasksRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: TasksRepository
) {
    suspend operator fun invoke(id: Int) = repository.deleteTaskById(id)
}
