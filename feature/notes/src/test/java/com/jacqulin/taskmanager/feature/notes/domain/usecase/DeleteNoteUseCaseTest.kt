package com.jacqulin.taskmanager.feature.notes.domain.usecase

import com.jacqulin.taskmanager.feature.notes.domain.repository.NotesRepository
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
class DeleteNoteUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private val notesRepository = mockk<NotesRepository>()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { notesRepository.deleteNoteById(any()) } returns Unit
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke should call repository deleteNoteById`() = runTest {
        val useCase = DeleteNoteUseCase(notesRepository)
        useCase(42)

        coVerify(exactly = 1) { notesRepository.deleteNoteById(42) }
    }

    @Test
    fun `invoke should pass correct note id to repository`() = runTest {
        val useCase = DeleteNoteUseCase(notesRepository)
        useCase(100)

        coVerify { notesRepository.deleteNoteById(100) }
    }
}
