package com.jacqulin.taskmanager.data.repository

import android.net.Uri
import com.jacqulin.taskmanager.core.storage.NoteImageStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NoteImageRepositoryImpl @Inject constructor(
    private val imageStorage: NoteImageStorage,
) : NoteImageRepository {

    override suspend fun saveImage(uri: Uri): String =
        imageStorage.saveImage(uri)

    override suspend fun deleteImage(path: String) =
        imageStorage.deleteImage(path)

    override suspend fun createTempImageUri(): Uri =
        imageStorage.createTempImageUri()

    override suspend fun deleteTempImage(uri: Uri) =
        imageStorage.deleteTempImage(uri)
}
