package com.jacqulin.taskmanager.core.domain.notes

import com.jacqulin.taskmanager.core.model.Note
import com.jacqulin.taskmanager.core.data.repository.NotesRepository
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(
    private val repository: NotesRepository
) {
    suspend operator fun invoke(id: Int): Note? = repository.getNoteById(id)
}
