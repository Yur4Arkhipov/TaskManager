package com.jacqulin.taskmanager.feature.notes.presentation.noteeditor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacqulin.taskmanager.feature.notes.domain.usecase.GetNoteByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getNoteUseCase: GetNoteByIdUseCase
) : ViewModel() {

    private val noteId: Int? = savedStateHandle.get<Int>("noteId")

    private val _uiState = MutableStateFlow(NoteEditorUiState())
    val uiState: StateFlow<NoteEditorUiState> = _uiState.asStateFlow()

    // Возможно обработать если null
    init {
        viewModelScope.launch {
            val id = noteId ?: return@launch
            getNoteUseCase(id).collect { note ->
                if (note != null) {
                    _uiState.update {
                        it.copy(
                            title = note.title,
                            content = note.content,
                            hasImage = note.hasPreviewImage,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun onTitleChanged(value: String) {
        _uiState.update { it.copy(title = value) }
    }

    fun onContentChanged(value: String) {
        _uiState.update { it.copy(content = value) }
    }

    fun onImageAdded() {
        _uiState.update { it.copy(hasImage = true) }
    }

    fun onImageRemoved() {
        _uiState.update { it.copy(hasImage = false) }
    }
}
