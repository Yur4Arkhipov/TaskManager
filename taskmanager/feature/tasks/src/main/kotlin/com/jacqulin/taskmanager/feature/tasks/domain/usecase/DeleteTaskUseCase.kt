package com.jacqulin.taskmanager.feature.tasks.domain.usecase

import com.jacqulin.taskmanager.feature.tasks.domain.repository.TasksRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: TasksRepository
) {
    suspend operator fun invoke(id: Int) = repository.deleteTaskById(id)
}
