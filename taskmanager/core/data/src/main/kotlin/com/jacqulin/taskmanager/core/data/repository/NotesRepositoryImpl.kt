package com.jacqulin.taskmanager.core.data.repository

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
    private val dao: NoteDao,
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
}
