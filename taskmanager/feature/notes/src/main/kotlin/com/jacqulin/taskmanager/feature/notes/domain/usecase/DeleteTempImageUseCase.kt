package com.jacqulin.taskmanager.feature.notes.domain.usecase

import android.net.Uri
import com.jacqulin.taskmanager.feature.notes.domain.repository.NoteImageStorage
import javax.inject.Inject

class DeleteTempImageUseCase @Inject constructor(
    private val imageStorage: NoteImageStorage
) {
    suspend operator fun invoke(uri: Uri) = imageStorage.deleteTempImage(uri)
}
