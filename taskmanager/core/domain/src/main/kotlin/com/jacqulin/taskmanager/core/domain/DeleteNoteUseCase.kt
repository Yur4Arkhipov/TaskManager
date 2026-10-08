package com.jacqulin.taskmanager.core.domain

import com.jacqulin.taskmanager.data.repository.NotesRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val repository: NotesRepository
) {
    suspend operator fun invoke(id: Int) = repository.deleteNoteById(id)
}
