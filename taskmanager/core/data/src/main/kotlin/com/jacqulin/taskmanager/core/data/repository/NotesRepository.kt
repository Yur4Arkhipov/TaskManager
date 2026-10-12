package com.jacqulin.taskmanager.core.data.repository

import com.jacqulin.taskmanager.core.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun observeNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: Int): Note?
    suspend fun addNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNoteById(noteId: Int)
}
