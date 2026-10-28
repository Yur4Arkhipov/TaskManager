package com.jacqulin.taskmanager.core.domain.tasks

import com.jacqulin.taskmanager.core.model.Task
import com.jacqulin.taskmanager.core.data.repository.TasksRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTasksUseCase @Inject constructor(
    private val repository: TasksRepository
) {
    operator fun invoke(): Flow<List<Task>> = repository.observeTasks()
}
