package com.jacqulin.taskmanager.feature.tasks.domain.usecase

import com.jacqulin.taskmanager.feature.tasks.domain.model.Task
import com.jacqulin.taskmanager.feature.tasks.domain.repository.TasksRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SaveTaskUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private val tasksRepository = mockk<TasksRepository>()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { tasksRepository.createTask(any()) } returns Unit
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke should call repository createTask`() = runTest {
        val task = Task(
            id = 0,
            title = "New Task",
            createdAtMillis = System.currentTimeMillis(),
            isCompleted = false
        )

        val useCase = SaveTaskUseCase(tasksRepository)
        useCase(task)

        coVerify(exactly = 1) { tasksRepository.createTask(task) }
    }

    @Test
    fun `invoke should pass task with correct properties`() = runTest {
        val task = Task(
            id = 0,
            title = "Test Task",
            createdAtMillis = 12345L,
            isCompleted = false
        )

        val useCase = SaveTaskUseCase(tasksRepository)
        useCase(task)

        coVerify { tasksRepository.createTask(match { it.title == "Test Task" && it.createdAtMillis == 12345L })}
    }
}
