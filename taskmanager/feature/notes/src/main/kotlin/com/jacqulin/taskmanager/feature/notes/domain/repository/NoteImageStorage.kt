package com.jacqulin.taskmanager.feature.notes.domain.repository

import android.net.Uri

interface NoteImageStorage {
    suspend fun saveImage(uri: Uri): String
    suspend fun deleteImage(path: String)
    suspend fun createTempImageUri(): Uri
    suspend fun deleteTempImage(uri: Uri)
}
