package com.jacqulin.taskmanager.core.domain

import android.net.Uri
import com.jacqulin.taskmanager.core.model.Note
import com.jacqulin.taskmanager.data.repository.NotesRepository
import javax.inject.Inject

class SaveNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository
) {

    suspend operator fun invoke(
        note: Note,
//        selectedImageUri: Uri?,
//        isImageRemoved: Boolean,
        isNewNote: Boolean
    ) {
//        val oldImagePath = note.imagePath
//
//        val newImagePath = when {
//            isImageRemoved -> null
//            selectedImageUri != null -> {
//                imageStorage.saveImage(selectedImageUri)
//            }
//            else -> {
//                oldImagePath
//            }
//        }
//
//        val noteToSave = note.copy(
//            imagePath = newImagePath,
//        )
//
//        try {
            if (isNewNote) {
                notesRepository.addNote(note)
            } else {
                notesRepository.updateNote(note)
            }

//            if (oldImagePath != null && oldImagePath != newImagePath) {
//                imageStorage.deleteImage(oldImagePath)
            }
//        } catch (e: Exception) {
//            if (newImagePath != null && newImagePath != oldImagePath) {
//                imageStorage.deleteImage(newImagePath)
//            }
//            throw e
//        }
//    }
}

//class SaveNoteUseCase @Inject constructor(
//    private val notesRepository: NotesRepository,
//    private val imageStorage: NoteImageStorage
//) {
//
//    suspend operator fun invoke(
//        note: Note,
//        selectedImageUri: Uri?,
//        isImageRemoved: Boolean,
//        isNewNote: Boolean
//    ) {
//        val oldImagePath = note.imagePath
//
//        val newImagePath = when {
//            isImageRemoved -> null
//            selectedImageUri != null -> {
//                imageStorage.saveImage(selectedImageUri)
//            }
//            else -> {
//                oldImagePath
//            }
//        }
//
//        val noteToSave = note.copy(
//            imagePath = newImagePath,
//        )
//
//        try {
//            if (isNewNote) {
//                notesRepository.addNote(noteToSave)
//            } else {
//                notesRepository.updateNote(noteToSave)
//            }
//
//            if (oldImagePath != null && oldImagePath != newImagePath) {
//                imageStorage.deleteImage(oldImagePath)
//            }
//        } catch (e: Exception) {
//            if (newImagePath != null && newImagePath != oldImagePath) {
//                imageStorage.deleteImage(newImagePath)
//            }
//            throw e
//        }
//    }
//}