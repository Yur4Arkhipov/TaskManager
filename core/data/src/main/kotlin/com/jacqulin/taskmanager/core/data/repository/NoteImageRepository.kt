package com.jacqulin.taskmanager.core.data.repository

import android.net.Uri

interface NoteImageRepository {
    suspend fun saveImage(uri: Uri): String
    suspend fun deleteImage(path: String)
    suspend fun createTempImageUri(): Uri
    suspend fun deleteTempImage(uri: Uri)
}
