package com.jacqulin.taskmanager.core.domain.notes

import com.jacqulin.taskmanager.core.model.Note
import com.jacqulin.taskmanager.data.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveNotesUseCase @Inject constructor(
    private val repository: NotesRepository
) {
    operator fun invoke(): Flow<List<Note>> = repository.observeNotes()
}
