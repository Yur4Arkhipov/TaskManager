package com.jacqulin.taskmanager.core.domain.notes

import android.net.Uri
import com.jacqulin.taskmanager.data.repository.NoteImageRepository
import javax.inject.Inject

class CreateTempImageUseCase @Inject constructor(
    private val noteImageRepository: NoteImageRepository
) {
    suspend operator fun invoke(): Uri = noteImageRepository.createTempImageUri()
}

