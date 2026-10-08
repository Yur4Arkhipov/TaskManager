package com.jacqulin.taskmanager.core.domain.tasks

import com.jacqulin.taskmanager.core.model.Task
import com.jacqulin.taskmanager.data.repository.TasksRepository
import javax.inject.Inject

class SaveTaskUseCase @Inject constructor(
    private val repository: TasksRepository
) {
    suspend operator fun invoke(task: Task) {
        repository.createTask(task)
    }
}
