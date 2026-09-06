package com.jacqulin.taskmanager.feature.notes.domain.repository

import com.jacqulin.taskmanager.feature.notes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun getAllNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: Int): Note?
    suspend fun addNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNoteById(noteId: Int)
}
