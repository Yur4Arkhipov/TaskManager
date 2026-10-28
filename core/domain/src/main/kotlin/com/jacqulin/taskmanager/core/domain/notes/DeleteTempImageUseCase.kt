package com.jacqulin.taskmanager.core.domain.notes

import android.net.Uri
import com.jacqulin.taskmanager.core.data.repository.NoteImageRepository
import javax.inject.Inject

class DeleteTempImageUseCase @Inject constructor(
    private val noteImageRepository: NoteImageRepository
) {
    suspend operator fun invoke(uri: Uri) = noteImageRepository.deleteTempImage(uri)
}
