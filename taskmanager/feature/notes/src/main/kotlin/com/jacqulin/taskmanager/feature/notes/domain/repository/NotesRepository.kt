package com.jacqulin.taskmanager.feature.notes.domain.repository

import com.jacqulin.taskmanager.feature.notes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun getAllNotes(): Flow<List<Note>>
    fun getNoteById(id: Int): Flow<Note?>
//    suspend fun getNoteById(id: Int): Note?
    suspend fun addNote(note: Note): Int
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(id: Int)
}
