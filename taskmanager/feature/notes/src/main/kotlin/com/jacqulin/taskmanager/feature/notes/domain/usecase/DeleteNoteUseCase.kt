package com.jacqulin.taskmanager.feature.notes.domain.usecase

import com.jacqulin.taskmanager.feature.notes.domain.repository.NotesRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val repository: NotesRepository
) {
    suspend operator fun invoke(id: Int) = repository.deleteNote(id)
}
