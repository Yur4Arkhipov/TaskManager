package com.jacqulin.taskmanager.feature.notes.presentation.noteeditor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacqulin.taskmanager.feature.notes.domain.model.Note
import com.jacqulin.taskmanager.feature.notes.domain.usecase.AddNoteUseCase
import com.jacqulin.taskmanager.feature.notes.domain.usecase.GetNoteByIdUseCase
import com.jacqulin.taskmanager.feature.notes.domain.usecase.UpdateNoteUseCase
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
    private val addNoteUseCase: AddNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase
) : ViewModel() {

    private val noteId: Int? = savedStateHandle.get<Int>("noteId")

    private val _uiState = MutableStateFlow(NoteEditorUiState())
    val uiState: StateFlow<NoteEditorUiState> = _uiState.asStateFlow()

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
            NoteEditorEvent.ImageAdded -> {
                _uiState.update {
                    it.copy(hasImage = true)
                }
            }
            NoteEditorEvent.ImageRemoved -> {
                _uiState.update {
                    it.copy(hasImage = false)
                }
            }
            NoteEditorEvent.SaveClicked -> {
                saveNote()
            }
            NoteEditorEvent.BackClicked -> {
                emitEffect(NoteEditorEffect.NavigateBack)
            }
        }
    }

    private fun loadNote() {
        val id = noteId ?: run {
            _uiState.update {
                it.copy(isLoading = false)
            }
            return
        }

        viewModelScope.launch {
            val note = getNoteUseCase(id)

            if (note == null) {
                _uiState.update {
                    it.copy(isLoading = false)
                }

                emitEffect(
                    NoteEditorEffect.ShowError("Не удалось загрузить заметку")
                )

                return@launch
            }

            _uiState.update {
                it.copy(
                    title = note.title,
                    content = note.content,
                    hasImage = note.hasPreviewImage,
                    isLoading = false,
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
            hasPreviewImage = state.hasImage,
            createdAtMillis = System.currentTimeMillis(),
        )

        viewModelScope.launch {
            if (noteId == null) {
                addNoteUseCase(note)
            } else {
                updateNoteUseCase(note)
            }

            emitEffect(NoteEditorEffect.NavigateBack)
        }
    }

    private fun emitEffect(effect: NoteEditorEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
