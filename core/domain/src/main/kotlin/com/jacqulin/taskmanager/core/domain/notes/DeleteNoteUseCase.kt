package com.jacqulin.taskmanager.core.domain.notes

import com.jacqulin.taskmanager.core.data.repository.NotesRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val repository: NotesRepository
) {
    suspend operator fun invoke(id: Int) = repository.deleteNoteById(id)
}
