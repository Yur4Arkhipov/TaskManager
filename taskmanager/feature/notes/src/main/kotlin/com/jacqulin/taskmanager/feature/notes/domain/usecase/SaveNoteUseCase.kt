package com.jacqulin.taskmanager.feature.notes.domain.usecase

import android.net.Uri
import com.jacqulin.taskmanager.feature.notes.domain.model.Note
import com.jacqulin.taskmanager.feature.notes.domain.repository.NoteImageStorage
import com.jacqulin.taskmanager.feature.notes.domain.repository.NotesRepository
import javax.inject.Inject

class SaveNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
    private val imageStorage: NoteImageStorage,
) {

    suspend operator fun invoke(
        note: Note,
        selectedImageUri: Uri?,
        isImageRemoved: Boolean,
        isNewNote: Boolean,
    ) {
        val oldImagePath = note.imagePath

        val newImagePath = when {
            isImageRemoved -> null
            selectedImageUri != null -> {
                imageStorage.saveImage(selectedImageUri)
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
                imageStorage.deleteImage(oldImagePath)
            }
        } catch (e: Exception) {
            if (newImagePath != null && newImagePath != oldImagePath) {
                imageStorage.deleteImage(newImagePath)
            }
            throw e
        }
    }
}
