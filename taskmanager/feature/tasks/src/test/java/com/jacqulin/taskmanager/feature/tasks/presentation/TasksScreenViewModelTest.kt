package com.jacqulin.taskmanager.feature.tasks.presentation

import app.cash.turbine.test
import com.jacqulin.taskmanager.core.voice.domain.VoiceRecognizer
import com.jacqulin.taskmanager.core.voice.domain.VoiceState
import com.jacqulin.taskmanager.feature.tasks.domain.model.Task
import com.jacqulin.taskmanager.feature.tasks.domain.usecase.DeleteTaskUseCase
import com.jacqulin.taskmanager.feature.tasks.domain.usecase.ObserveTasksUseCase
import com.jacqulin.taskmanager.feature.tasks.domain.usecase.SaveTaskUseCase
import com.jacqulin.taskmanager.feature.tasks.domain.usecase.UpdateTaskStatusUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TasksScreenViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val observeTasksUseCase = mockk<ObserveTasksUseCase>()
    private val deleteTaskUseCase = mockk<DeleteTaskUseCase>()
    private val saveTaskUseCase = mockk<SaveTaskUseCase>()
    private val updateTaskStatusUseCase = mockk<UpdateTaskStatusUseCase>()
    private val voiceRecognizer = mockk<VoiceRecognizer>()

    private val tasksFlow = MutableStateFlow<List<Task>>(emptyList())

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { observeTasksUseCase() } returns tasksFlow
        coEvery { deleteTaskUseCase(any()) } returns Unit
        coEvery { saveTaskUseCase(any()) } returns Unit
        coEvery { updateTaskStatusUseCase(any()) } returns Unit
        every { voiceRecognizer.start() } returns Unit
        coEvery { voiceRecognizer.stopAndRecognize() } returns Result.success("Test task")
        every { voiceRecognizer.cancel() } returns Unit
        every { voiceRecognizer.isRecordingActive } returns false
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial ui state should have empty tasks`() {
        val viewModel = TasksScreenViewModel(
            observeTasksUseCase,
            deleteTaskUseCase,
            saveTaskUseCase,
            updateTaskStatusUseCase,
            voiceRecognizer
        )
        assertTrue(viewModel.uiState.value.visibleTasks.isEmpty())
        assertTrue(viewModel.uiState.value.isEmpty)
    }

    @Test
    fun `ui state should show tasks when emitted`() = runTest {
        val task = Task(
            id = 1,
            title = "Test Task",
            createdAtMillis = 1000L,
            isCompleted = false
        )
        tasksFlow.tryEmit(listOf(task))
        testDispatcher.scheduler.advanceUntilIdle()

        val viewModel = TasksScreenViewModel(
            observeTasksUseCase,
            deleteTaskUseCase,
            saveTaskUseCase,
            updateTaskStatusUseCase,
            voiceRecognizer
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, viewModel.uiState.value.visibleTasks.size)
        assertEquals("Test Task", viewModel.uiState.value.visibleTasks[0].title)
    }

    @Test
    fun `tasks should be filtered by search query`() = runTest {
        val task1 = Task(id = 1, title = "Buy milk", createdAtMillis = 1000L, isCompleted = false)
        val task2 = Task(id = 2, title = "Write code", createdAtMillis = 2000L, isCompleted = false)
        tasksFlow.tryEmit(listOf(task1, task2))
        testDispatcher.scheduler.advanceUntilIdle()

        val viewModel = TasksScreenViewModel(
            observeTasksUseCase,
            deleteTaskUseCase,
            saveTaskUseCase,
            updateTaskStatusUseCase,
            voiceRecognizer
        )
        testDispatcher.scheduler.advanceUntilIdle()

        // No filter - should show all
        assertEquals(2, viewModel.uiState.value.visibleTasks.size)

        // Apply filter
        viewModel.onEvent(TasksEvent.OnSearchQueryChanged("buy"))
        viewModel.onEvent(TasksEvent.OnSearchSubmitted)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, viewModel.uiState.value.visibleTasks.size)
        assertEquals("Buy milk", viewModel.uiState.value.visibleTasks[0].title)
    }

    @Test
    fun `completed tasks should be sorted to bottom by default`() = runTest {
        val completedTask = Task(id = 1, title = "Done", createdAtMillis = 1000L, isCompleted = true)
        val activeTask = Task(id = 2, title = "Active", createdAtMillis = 2000L, isCompleted = false)
        tasksFlow.tryEmit(listOf(completedTask, activeTask))
        testDispatcher.scheduler.advanceUntilIdle()

        val viewModel = TasksScreenViewModel(
            observeTasksUseCase,
            deleteTaskUseCase,
            saveTaskUseCase,
            updateTaskStatusUseCase,
            voiceRecognizer
        )
        testDispatcher.scheduler.advanceUntilIdle()

        // Active task should be first, completed at bottom
        assertFalse(viewModel.uiState.value.visibleTasks[0].isCompleted)
        assertTrue(viewModel.uiState.value.visibleTasks[1].isCompleted)
    }

    @Test
    fun `create draft task should set draft task`() = runTest {
        val viewModel = TasksScreenViewModel(
            observeTasksUseCase,
            deleteTaskUseCase,
            saveTaskUseCase,
            updateTaskStatusUseCase,
            voiceRecognizer
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertNull(viewModel.uiState.value.draftTask)

        viewModel.onEvent(TasksEvent.OnCreateTaskByTextClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.draftTask)
    }

    @Test
    fun `draft task text should be updated`() = runTest {
        val viewModel = TasksScreenViewModel(
            observeTasksUseCase,
            deleteTaskUseCase,
            saveTaskUseCase,
            updateTaskStatusUseCase,
            voiceRecognizer
        )
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(TasksEvent.OnCreateTaskByTextClicked)
        viewModel.onEvent(TasksEvent.OnDraftTaskTextChanged("New task title"))
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("New task title", viewModel.uiState.value.draftTask?.title)
    }

    @Test
    fun `saving draft task should call save use case`() = runTest {
        val viewModel = TasksScreenViewModel(
            observeTasksUseCase,
            deleteTaskUseCase,
            saveTaskUseCase,
            updateTaskStatusUseCase,
            voiceRecognizer
        )
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(TasksEvent.OnCreateTaskByTextClicked)
        viewModel.onEvent(TasksEvent.OnDraftTaskTextChanged("Task to save"))
        viewModel.onEvent(TasksEvent.OnDraftTaskSaveClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 1) { saveTaskUseCase(any()) }
        assertNull(viewModel.uiState.value.draftTask)
    }

    @Test
    fun `deleting draft task should clear draft`() = runTest {
        val viewModel = TasksScreenViewModel(
            observeTasksUseCase,
            deleteTaskUseCase,
            saveTaskUseCase,
            updateTaskStatusUseCase,
            voiceRecognizer
        )
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(TasksEvent.OnCreateTaskByTextClicked)
        viewModel.onEvent(TasksEvent.OnDraftTaskDeleteClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        assertNull(viewModel.uiState.value.draftTask)
    }

    @Test
    fun `deleting task should call delete use case`() = runTest {
        val task = Task(id = 1, title = "Task", createdAtMillis = 1000L, isCompleted = false)
        tasksFlow.tryEmit(listOf(task))
        testDispatcher.scheduler.advanceUntilIdle()

        val viewModel = TasksScreenViewModel(
            observeTasksUseCase,
            deleteTaskUseCase,
            saveTaskUseCase,
            updateTaskStatusUseCase,
            voiceRecognizer
        )
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(TasksEvent.OnDeleteTaskClicked(1))
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 1) { deleteTaskUseCase(1) }
    }

//    @Test
//    fun `updating task status should call update use case`() = runTest {
//        val task = TaskItemUi(id = 1, title = "Task", createdAtMillis = 1000L, isCompleted = false)
//        tasksFlow.tryEmit(Task(1, "Task", 1000L, false))
//        testDispatcher.scheduler.advanceUntilIdle()
//
//        val viewModel = TasksScreenViewModel(
//            observeTasksUseCase,
//            deleteTaskUseCase,
//            saveTaskUseCase,
//            updateTaskStatusUseCase,
//            voiceRecognizer
//        )
//        testDispatcher.scheduler.advanceUntilIdle()
//
//        viewModel.onEvent(TasksEvent.UpdateTaskStatus(task))
//        testDispatcher.scheduler.advanceUntilIdle()
//
//        coVerify(exactly = 1) { updateTaskStatusUseCase(any()) }
//    }

    @Test
    fun `voice permission requested should emit effect`() = runTest {
        val viewModel = TasksScreenViewModel(
            observeTasksUseCase,
            deleteTaskUseCase,
            saveTaskUseCase,
            updateTaskStatusUseCase,
            voiceRecognizer
        )
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(TasksEvent.OnCreateTaskByVoiceClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.effects.test {
            val effect = awaitItem()
            assertTrue(effect is TasksEffect.RequestVoicePermission)
        }
    }

    @Test
    fun `voice permission denied should emit error effect`() = runTest {
        val viewModel = TasksScreenViewModel(
            observeTasksUseCase,
            deleteTaskUseCase,
            saveTaskUseCase,
            updateTaskStatusUseCase,
            voiceRecognizer
        )
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(TasksEvent.VoicePermissionDenied)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.effects.test {
            val effect = awaitItem()
            assertTrue(effect is TasksEffect.ShowError)
        }
    }

    @Test
    fun `voice text recognized should set draft task`() = runTest {
        val viewModel = TasksScreenViewModel(
            observeTasksUseCase,
            deleteTaskUseCase,
            saveTaskUseCase,
            updateTaskStatusUseCase,
            voiceRecognizer
        )
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(TasksEvent.VoiceTextRecognized("Voice task"))
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Voice task", viewModel.uiState.value.draftTask?.title)
        assertEquals(VoiceState.Success("Voice task"), viewModel.uiState.value.voiceState)
    }

    @Test
    fun `empty draft should not be saved`() = runTest {
        val viewModel = TasksScreenViewModel(
            observeTasksUseCase,
            deleteTaskUseCase,
            saveTaskUseCase,
            updateTaskStatusUseCase,
            voiceRecognizer
        )
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(TasksEvent.OnCreateTaskByTextClicked)
        viewModel.onEvent(TasksEvent.OnDraftTaskSaveClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        // Draft should still be present (not saved because empty)
        assertNotNull(viewModel.uiState.value.draftTask)
        coVerify(exactly = 0) { saveTaskUseCase(any()) }
    }

    @Test
    fun `draft task with whitespace only should not be saved`() = runTest {
        val viewModel = TasksScreenViewModel(
            observeTasksUseCase,
            deleteTaskUseCase,
            saveTaskUseCase,
            updateTaskStatusUseCase,
            voiceRecognizer
        )
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(TasksEvent.OnCreateTaskByTextClicked)
        viewModel.onEvent(TasksEvent.OnDraftTaskTextChanged("   "))
        viewModel.onEvent(TasksEvent.OnDraftTaskSaveClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        // Draft should still be present (not saved because whitespace only)
        assertNotNull(viewModel.uiState.value.draftTask)
        coVerify(exactly = 0) { saveTaskUseCase(any()) }
    }
}
