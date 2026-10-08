package com.jacqulin.taskmanager.data.repository

import android.net.Uri
import com.jacqulin.taskmanager.core.database.dao.NoteDao
import com.jacqulin.taskmanager.core.database.entity.asExternalModel
import com.jacqulin.taskmanager.core.database.entity.toEntity
import com.jacqulin.taskmanager.core.model.Note
import com.jacqulin.taskmanager.core.storage.NoteImageStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NotesRepositoryImpl @Inject constructor(
    private val dao: NoteDao,
    private val imageStorage: NoteImageStorage
) : NotesRepository {

    override fun observeNotes(): Flow<List<Note>> {
        return dao.observeNotes()
            .map { notes ->
                notes.map { it.asExternalModel() }
            }
    }

    override suspend fun getNoteById(id: Int): Note? {
        return dao.getNoteById(id)?.asExternalModel()
    }

    override suspend fun addNote(note: Note) {
        return dao.insertNote(note.toEntity())
    }

    override suspend fun updateNote(note: Note) {
        return dao.updateNote(note.toEntity())
    }

    override suspend fun deleteNoteById(noteId: Int) {
        return dao.deleteNoteById(noteId)
    }

//    override suspend fun addNote(note: Note, imageUri: Uri?) {
//        val imagePath = imageUri?.let { imageStorage.saveImage(it) }
//        dao.insertNote(note.copy(imagePath = imagePath).toEntity())
//    }

//    override suspend fun updateNote(note: Note, imageUri: Uri?) {
//        val oldNote = dao.getNoteById(note.id)
//        val oldPath = oldNote?.imagePath
//
//        val newPath = when {
//            imageUri == null -> oldPath
//            else -> imageStorage.saveImage(imageUri)
//        }
//
//        if (oldPath != null && oldPath != newPath) {
//            imageStorage.deleteImage(oldPath)
//        }
//
//        dao.updateNote(note.copy(imagePath = newPath).toEntity())
//    }

//    override suspend fun deleteNoteById(noteId: Int) {
//        val note = dao.getNoteById(noteId)
//        note?.imagePath?.let { imageStorage.deleteImage(it) }
//        dao.deleteNoteById(noteId)
//    }
}
