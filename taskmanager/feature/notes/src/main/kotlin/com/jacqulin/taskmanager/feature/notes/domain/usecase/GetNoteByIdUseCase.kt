package com.jacqulin.taskmanager.feature.notes.domain.usecase

import com.jacqulin.taskmanager.feature.notes.domain.model.Note
import com.jacqulin.taskmanager.feature.notes.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(
    private val repository: NotesRepository
) {
    operator fun invoke(id: Int): Flow<Note?> = repository.getNoteById(id)
}
