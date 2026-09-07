package com.jacqulin.taskmanager.feature.notes.data.repository

import android.content.Context
import android.net.Uri
import com.jacqulin.taskmanager.feature.notes.domain.repository.NoteImageStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class NoteImageStorageImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : NoteImageStorage {

    override suspend fun saveImage(uri: Uri): String =
        withContext(Dispatchers.IO) {
            val imagesDir = File(
                context.filesDir,
                "note_images"
            ).apply {
                mkdirs()
            }

            val file = File(
                imagesDir,
                "${UUID.randomUUID()}.jpg"
            )

            context.contentResolver
                .openInputStream(uri)
                ?.use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                ?: throw IOException("Unable to open image")

            file.absolutePath
        }

    override suspend fun deleteImage(path: String) {
        withContext(Dispatchers.IO) {
            File(path).delete()
        }
    }
}
