package com.jacqulin.taskmanager.feature.notes.presentation.noteeditor

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacqulin.taskmanager.feature.notes.domain.model.Note
import com.jacqulin.taskmanager.feature.notes.domain.repository.NoteImageStorage
import com.jacqulin.taskmanager.feature.notes.domain.usecase.GetNoteByIdUseCase
import com.jacqulin.taskmanager.feature.notes.domain.usecase.SaveNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getNoteUseCase: GetNoteByIdUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
    private val noteImageStorage: NoteImageStorage
) : ViewModel() {

    private val noteId: Int? = savedStateHandle.get<Int>("noteId")

    private val _uiState = MutableStateFlow(NoteEditorUiState())
    val uiState: StateFlow<NoteEditorUiState> = _uiState.asStateFlow()

    private var currentTempImageUri: Uri? = null

    private val _effects = MutableSharedFlow<NoteEditorEffect>()
    val effects: SharedFlow<NoteEditorEffect> = _effects.asSharedFlow()

    init {
        loadNote()
    }

    fun onEvent(event: NoteEditorEvent) {
        when (event) {
            is NoteEditorEvent.TitleChanged -> {
                _uiState.update {
                    it.copy(
                        title = event.value,
                        titleError = null
                    )
                }
            }
            is NoteEditorEvent.ContentChanged -> {
                _uiState.update {
                    it.copy(content = event.value)
                }
            }
            is NoteEditorEvent.ImageSelected -> {
                _uiState.update {
                    it.copy(
                        selectedImageUri = event.uri,
                        isImageRemoved = false,
                    )
                }
            }
            is NoteEditorEvent.CameraCancelled -> {
                deleteTempImage(event.uri)
            }
            NoteEditorEvent.ImageAddFromGalleryClicked -> {
                emitEffect(NoteEditorEffect.LaunchGallery)
            }
            NoteEditorEvent.ImageAddFromCameraClicked -> {
                emitEffect(NoteEditorEffect.RequestCameraPermission)
            }
            NoteEditorEvent.CameraPermissionGranted -> {
                viewModelScope.launch {
                    currentTempImageUri = noteImageStorage.createTempImageUri()
                    emitEffect(NoteEditorEffect.LaunchCamera(currentTempImageUri!!))
                }
            }
            NoteEditorEvent.CameraPermissionDenied -> {
                emitEffect(
                    NoteEditorEffect.ShowError(
                        "Для использования камеры необходимо предоставить разрешение",
                    ),
                )
            }
            NoteEditorEvent.ImageRemoved -> {
                _uiState.update {
                    it.copy(
                        selectedImageUri = null,
                        isImageRemoved = true
                    )
                }
            }
            NoteEditorEvent.SaveClicked -> {
                saveNote()
            }
            is NoteEditorEvent.BackClicked -> {
                viewModelScope.launch {
                    currentTempImageUri?.let { uri ->
                        noteImageStorage.deleteTempImage(uri)
                    }

                    currentTempImageUri = null

                    emitEffect(NoteEditorEffect.NavigateBack)
                }
            }
        }
    }

    private fun loadNote() {
        val id = noteId ?: run {
            _uiState.update { it.copy(isLoading = false) }
            return
        }

        viewModelScope.launch {
            val note = getNoteUseCase(id)

            if (note == null) {
                _uiState.update { it.copy(isLoading = false) }
                emitEffect(NoteEditorEffect.ShowError("Не удалось загрузить заметку"))
                return@launch
            }

            _uiState.update {
                it.copy(
                    title = note.title,
                    content = note.content,
                    imagePath = note.imagePath,
                    createdAtMillis = note.createdAtMillis,
                    isLoading = false
                )
            }
        }
    }

    private fun saveNote() {
        val state = _uiState.value

        if (state.title.isBlank()) {
            _uiState.update {
                it.copy(
                    titleError = "Заголовок не может быть пустым"
                )
            }
            return
        }

        val note = Note(
            id = noteId ?: 0,
            title = state.title,
            content = state.content,
            imagePath = state.imagePath,
            createdAtMillis = if (noteId == null) {
                System.currentTimeMillis()
            } else {
                state.createdAtMillis
            }
        )

        viewModelScope.launch {
            try {
                saveNoteUseCase(
                    note = note,
                    selectedImageUri = state.selectedImageUri,
                    isImageRemoved = state.isImageRemoved,
                    isNewNote = noteId == null,
                )

                if (currentTempImageUri != null) {
                    deleteTempImage(currentTempImageUri!!)
                    currentTempImageUri = null
                }

                emitEffect(NoteEditorEffect.NavigateBack)
            } catch (_: Exception) {
                emitEffect(
                    NoteEditorEffect.ShowError(
                        "Не удалось сохранить заметку",
                    ),
                )
            }
        }
    }

    private fun deleteTempImage(uri: Uri) {
        viewModelScope.launch {
            noteImageStorage.deleteTempImage(uri)
            if (currentTempImageUri == uri) {
                currentTempImageUri = null
            }
        }
    }

    private fun emitEffect(effect: NoteEditorEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
