package com.jacqulin.taskmanager.feature.tasks.domain.usecase

import app.cash.turbine.test
import com.jacqulin.taskmanager.feature.tasks.domain.model.Task
import com.jacqulin.taskmanager.feature.tasks.domain.repository.TasksRepository
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ObserveTasksUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private val tasksRepository = mockk<TasksRepository>()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke should return tasks flow from repository`() = runTest {
        val tasks = listOf(
            Task(id = 1, title = "Task 1", createdAtMillis = 1000L, isCompleted = false),
            Task(id = 2, title = "Task 2", createdAtMillis = 2000L, isCompleted = true)
        )
        every { tasksRepository.observeTasks() } returns flowOf(tasks)

        val useCase = ObserveTasksUseCase(tasksRepository)

        useCase().test {
            val emitted = awaitItem()
            assertEquals(2, emitted.size)
            assertEquals("Task 1", emitted[0].title)
            assertEquals("Task 2", emitted[1].title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `invoke should return empty list when repository returns empty`() = runTest {
        every { tasksRepository.observeTasks() } returns flowOf(emptyList())

        val useCase = ObserveTasksUseCase(tasksRepository)

        useCase().test {
            val emitted = awaitItem()
            assertTrue(emitted.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
