package com.jacqulin.taskmanager.feature.notes.presentation.notebase

import app.cash.turbine.test
import com.jacqulin.taskmanager.core.designsystem.model.SortType
import com.jacqulin.taskmanager.feature.notes.domain.model.Note
import com.jacqulin.taskmanager.feature.notes.domain.usecase.DeleteNoteUseCase
import com.jacqulin.taskmanager.feature.notes.domain.usecase.ObserveNotesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
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
class NotesScreenViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val observeNotesUseCase = mockk<ObserveNotesUseCase>()
    private val deleteNoteUseCase = mockk<DeleteNoteUseCase>()

    private val notesFlow = MutableStateFlow<List<Note>>(emptyList())

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { observeNotesUseCase() } returns notesFlow
        coEvery { deleteNoteUseCase(any()) } returns Unit
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial ui state should have empty notes`() {
        val viewModel = NotesScreenViewModel(observeNotesUseCase, deleteNoteUseCase)
        assertTrue(viewModel.uiState.value.visibleNotes.isEmpty())
        assertTrue(viewModel.uiState.value.isEmpty)
    }

    @Test
    fun `ui state should show notes when emitted`() = runTest {
        val note = Note(
            id = 1,
            title = "Test Note",
            content = "Content",
            createdAtMillis = 1000L,
            imagePath = null
        )
        notesFlow.tryEmit(listOf(note))
        testDispatcher.scheduler.advanceUntilIdle()

        val viewModel = NotesScreenViewModel(observeNotesUseCase, deleteNoteUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, viewModel.uiState.value.visibleNotes.size)
        assertEquals("Test Note", viewModel.uiState.value.visibleNotes[0].title)
    }

    @Test
    fun `notes should be filtered by search query`() = runTest {
        val note1 = Note(id = 1, title = "Alpha", content = "", createdAtMillis = 1000L, imagePath = null)
        val note2 = Note(id = 2, title = "Beta", content = "", createdAtMillis = 2000L, imagePath = null)
        notesFlow.tryEmit(listOf(note1, note2))
        testDispatcher.scheduler.advanceUntilIdle()

        val viewModel = NotesScreenViewModel(observeNotesUseCase, deleteNoteUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // No filter - should show all
        assertEquals(2, viewModel.uiState.value.visibleNotes.size)

        // Apply filter
        viewModel.onEvent(NotesEvent.OnSearchQueryChanged("alpha"))
        viewModel.onEvent(NotesEvent.OnSearchSubmitted)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, viewModel.uiState.value.visibleNotes.size)
        assertEquals("Alpha", viewModel.uiState.value.visibleNotes[0].title)
    }

    @Test
    fun `notes should be sorted new to old by default`() = runTest {
        val note1 = Note(id = 1, title = "Old", content = "", createdAtMillis = 1000L, imagePath = null)
        val note2 = Note(id = 2, title = "New", content = "", createdAtMillis = 2000L, imagePath = null)
        notesFlow.tryEmit(listOf(note1, note2))
        testDispatcher.scheduler.advanceUntilIdle()

        val viewModel = NotesScreenViewModel(observeNotesUseCase, deleteNoteUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("New", viewModel.uiState.value.visibleNotes[0].title)
        assertEquals("Old", viewModel.uiState.value.visibleNotes[1].title)
    }

    @Test
    fun `notes should be sorted old to new when sort type changes`() = runTest {
        val note1 = Note(id = 1, title = "Old", content = "", createdAtMillis = 1000L, imagePath = null)
        val note2 = Note(id = 2, title = "New", content = "", createdAtMillis = 2000L, imagePath = null)
        notesFlow.tryEmit(listOf(note1, note2))
        testDispatcher.scheduler.advanceUntilIdle()

        val viewModel = NotesScreenViewModel(observeNotesUseCase, deleteNoteUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Change sort type
        viewModel.onEvent(NotesEvent.OnSortChanged(SortType.OLD_TO_NEW))
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Old", viewModel.uiState.value.visibleNotes[0].title)
        assertEquals("New", viewModel.uiState.value.visibleNotes[1].title)
    }

    @Test
    fun `delete mode toggle should work`() = runTest {
        val viewModel = NotesScreenViewModel(observeNotesUseCase, deleteNoteUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isDeleteModeEnabled)

        viewModel.onEvent(NotesEvent.OnDeleteModeToggled)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isDeleteModeEnabled)

        viewModel.onEvent(NotesEvent.OnDeleteModeToggled)
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isDeleteModeEnabled)
    }

    @Test
    fun `navigating to create note should emit effect`() = runTest {
        val viewModel = NotesScreenViewModel(observeNotesUseCase, deleteNoteUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(NotesEvent.OnCreateNoteClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.effects.test {
            val effect = awaitItem()
            assertTrue(effect is NotesEffect.NavigateToCreateNote)
        }
    }

    @Test
    fun `navigating to existing note should emit effect with note id`() = runTest {
        val viewModel = NotesScreenViewModel(observeNotesUseCase, deleteNoteUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(NotesEvent.OnNoteClicked(42))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.effects.test {
            val effect = awaitItem()
            assertTrue(effect is NotesEffect.NavigateToExistingNote)
            assertEquals(42, (effect as NotesEffect.NavigateToExistingNote).noteId)
        }
    }

    @Test
    fun `delete note should call use case`() = runTest {
        val note = Note(id = 1, title = "Test", content = "", createdAtMillis = 1000L, imagePath = null)
        notesFlow.tryEmit(listOf(note))
        testDispatcher.scheduler.advanceUntilIdle()

        val viewModel = NotesScreenViewModel(observeNotesUseCase, deleteNoteUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(NotesEvent.OnDeleteNoteClicked(1))
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 1) { deleteNoteUseCase(1) }
    }

    @Test
    fun `search events should be ignored when delete mode is enabled`() = runTest {
        val viewModel = NotesScreenViewModel(observeNotesUseCase, deleteNoteUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(NotesEvent.OnDeleteModeToggled)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(NotesEvent.OnSearchQueryChanged("test"))
        viewModel.onEvent(NotesEvent.OnSearchSubmitted)
        viewModel.onEvent(NotesEvent.OnCreateNoteClicked)
        viewModel.onEvent(NotesEvent.OnNoteClicked(1))
        testDispatcher.scheduler.advanceUntilIdle()

        // Effects should not be emitted for navigation events
        viewModel.effects.test {
            expectNoEvents()
        }
    }
}
