package com.jacqulin.taskmanager.feature.notes.domain.usecase

import com.jacqulin.taskmanager.feature.notes.domain.model.Note
import com.jacqulin.taskmanager.feature.notes.domain.repository.NotesRepository
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
    private val repository: NotesRepository
) {
    suspend operator fun invoke(note: Note) = repository.updateNote(note)
}
