package com.jacqulin.taskmanager.data.repository

import com.jacqulin.taskmanager.core.database.dao.NoteDao
import com.jacqulin.taskmanager.core.database.entity.asExternalModel
import com.jacqulin.taskmanager.core.database.entity.toEntity
import com.jacqulin.taskmanager.core.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NotesRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NotesRepository {

    override fun observeNotes(): Flow<List<Note>> {
        return noteDao.observeNotes()
            .map { notes ->
                notes.map { it.asExternalModel() }
            }
    }

    override suspend fun getNoteById(id: Int): Note? {
        return noteDao.getNoteById(id)?.asExternalModel()
    }

    override suspend fun addNote(note: Note) {
        return noteDao.insertNote(note.toEntity())
    }

    override suspend fun updateNote(note: Note) {
        return noteDao.updateNote(note.toEntity())
    }

    override suspend fun deleteNoteById(noteId: Int) {
        return noteDao.deleteNoteById(noteId)
    }
}
