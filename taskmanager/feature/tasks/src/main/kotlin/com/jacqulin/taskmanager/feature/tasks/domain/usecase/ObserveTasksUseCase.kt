package com.jacqulin.taskmanager.feature.tasks.domain.usecase

import com.jacqulin.taskmanager.feature.tasks.domain.model.Task
import com.jacqulin.taskmanager.feature.tasks.domain.repository.TasksRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTasksUseCase @Inject constructor(
    private val repository: TasksRepository
) {
    operator fun invoke(): Flow<List<Task>> = repository.observeTasks()
}
