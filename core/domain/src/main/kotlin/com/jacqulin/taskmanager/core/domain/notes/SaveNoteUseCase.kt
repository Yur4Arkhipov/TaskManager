package com.jacqulin.taskmanager.core.domain.notes

import android.net.Uri
import com.jacqulin.taskmanager.core.model.Note
import com.jacqulin.taskmanager.core.data.repository.NoteImageRepository
import com.jacqulin.taskmanager.core.data.repository.NotesRepository
import javax.inject.Inject

class SaveNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
    private val noteImageRepository: NoteImageRepository
) {

    suspend operator fun invoke(
        note: Note,
        selectedImageUri: Uri?,
        isImageRemoved: Boolean,
        isNewNote: Boolean
    ) {
        val oldImagePath = note.imagePath

        val newImagePath = when {
            isImageRemoved -> null
            selectedImageUri != null -> {
                noteImageRepository.saveImage(selectedImageUri)
            }
            else -> {
                oldImagePath
            }
        }

        val noteToSave = note.copy(
            imagePath = newImagePath,
        )

        try {
            if (isNewNote) {
                notesRepository.addNote(noteToSave)
            } else {
                notesRepository.updateNote(noteToSave)
            }

            if (oldImagePath != null && oldImagePath != newImagePath) {
                noteImageRepository.deleteImage(oldImagePath)
            }
        } catch (e: Exception) {
            if (newImagePath != null && newImagePath != oldImagePath) {
                noteImageRepository.deleteImage(newImagePath)
            }
            throw e
        }
    }
}
