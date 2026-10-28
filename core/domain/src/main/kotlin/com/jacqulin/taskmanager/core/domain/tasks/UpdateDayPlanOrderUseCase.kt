package com.jacqulin.taskmanager.core.domain.tasks

import com.jacqulin.taskmanager.core.data.repository.TasksRepository
import javax.inject.Inject

class UpdateDayPlanOrderUseCase @Inject constructor(
    private val repository: TasksRepository,
) {
    suspend operator fun invoke(taskIds: List<Int>) {
        repository.updateDayPlanOrder(taskIds)
    }
}
