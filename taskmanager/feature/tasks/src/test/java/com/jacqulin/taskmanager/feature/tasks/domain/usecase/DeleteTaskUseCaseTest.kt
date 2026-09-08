package com.jacqulin.taskmanager.feature.tasks.domain.usecase

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
class DeleteTaskUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private val tasksRepository = mockk<TasksRepository>()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { tasksRepository.deleteTaskById(any()) } returns Unit
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke should call repository deleteTaskById`() = runTest {
        val useCase = DeleteTaskUseCase(tasksRepository)
        useCase(42)

        coVerify(exactly = 1) { tasksRepository.deleteTaskById(42) }
    }

    @Test
    fun `invoke should pass correct task id to repository`() = runTest {
        val useCase = DeleteTaskUseCase(tasksRepository)
        useCase(100)

        coVerify { tasksRepository.deleteTaskById(100) }
    }
}
