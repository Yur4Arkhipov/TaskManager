package com.jacqulin.taskmanager.data.repository

import android.net.Uri
import com.jacqulin.taskmanager.core.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun observeNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: Int): Note?
    suspend fun addNote(note: Note)
//    suspend fun addNote(note: Note, imageUri: Uri? = null)
    suspend fun updateNote(note: Note)
//    suspend fun updateNote(note: Note, imageUri: Uri? = null)
    suspend fun deleteNoteById(noteId: Int)
}
