package com.jacqulin.taskmanager.feature.notes.presentation.noteeditor

import android.net.Uri

/**
 *  imagePath - картинка сохраненная в памяти устройства
 *  selectedImageUri - превью не сохраненное в памяти
 */
data class NoteEditorUiState(
    val title: String = "",
    val titleError: String? = null,
    val content: String = "",
    val imagePath: String? = null,
    val selectedImageUri: Uri? = null,
    val isImageRemoved: Boolean = false,
    val createdAtMillis: Long = 0L,
    val isLoading: Boolean = true
)
