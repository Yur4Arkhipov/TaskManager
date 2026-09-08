package com.jacqulin.taskmanager.feature.notes.domain.usecase

import app.cash.turbine.test
import com.jacqulin.taskmanager.feature.notes.domain.model.Note
import com.jacqulin.taskmanager.feature.notes.domain.repository.NotesRepository
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
class ObserveNotesUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private val notesRepository = mockk<NotesRepository>()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke should return notes flow from repository`() = runTest {
        val notes = listOf(
            Note(id = 1, title = "Note 1", content = "Content 1", createdAtMillis = 1000L, imagePath = null),
            Note(id = 2, title = "Note 2", content = "Content 2", createdAtMillis = 2000L, imagePath = "path.jpg")
        )
        every { notesRepository.observeNotes() } returns flowOf(notes)

        val useCase = ObserveNotesUseCase(notesRepository)

        useCase().test {
            val emitted = awaitItem()
            assertEquals(2, emitted.size)
            assertEquals("Note 1", emitted[0].title)
            assertEquals("Note 2", emitted[1].title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `invoke should return empty list when repository returns empty`() = runTest {
        every { notesRepository.observeNotes() } returns flowOf(emptyList())

        val useCase = ObserveNotesUseCase(notesRepository)

        useCase().test {
            val emitted = awaitItem()
            assertTrue(emitted.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
