package com.jacqulin.taskmanager.feature.notes.data.repository

import com.jacqulin.taskmanager.core.database.dao.NoteDao
import com.jacqulin.taskmanager.feature.notes.data.mapper.toDomain
import com.jacqulin.taskmanager.feature.notes.data.mapper.toEntity
import com.jacqulin.taskmanager.feature.notes.domain.model.Note
import com.jacqulin.taskmanager.feature.notes.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NotesRepository {

    override fun observeNotes(): Flow<List<Note>> {
        return noteDao.observeNotes()
            .map { notes ->
                notes.map { it.toDomain() }
            }
    }

    override suspend fun getNoteById(id: Int): Note? {
        return noteDao.getNoteById(id)?.toDomain()
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
