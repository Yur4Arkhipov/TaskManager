package com.jacqulin.taskmanager.feature.notes.presentation.noteeditor

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacqulin.taskmanager.core.voice.domain.VoiceError
import com.jacqulin.taskmanager.core.voice.domain.VoiceRecognizer
import com.jacqulin.taskmanager.core.voice.domain.VoiceState
import com.jacqulin.taskmanager.feature.notes.domain.model.Note
import com.jacqulin.taskmanager.feature.notes.domain.usecase.CreateTempImageUseCase
import com.jacqulin.taskmanager.feature.notes.domain.usecase.DeleteTempImageUseCase
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
    private val createTempImageUseCase: CreateTempImageUseCase,
    private val deleteTempImageUseCase: DeleteTempImageUseCase,
    private val voiceRecognizer: VoiceRecognizer,
) : ViewModel() {

    private val noteId: Int? = savedStateHandle.get<Int>("noteId")

    private val _uiState = MutableStateFlow(NoteEditorUiState())
    val uiState: StateFlow<NoteEditorUiState> = _uiState.asStateFlow()

    private var currentTempImageUri: Uri? = null

    private val _effects = MutableSharedFlow<NoteEditorEffect>()
    val effects: SharedFlow<NoteEditorEffect> = _effects.asSharedFlow()

    init {
        loadNote()
        observeVoiceState()
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
                    currentTempImageUri = createTempImageUseCase()
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
            NoteEditorEvent.VoiceInputStartClicked -> {
                Log.d("NoteEditorVM", "request permission voice recognizer")
                emitEffect(NoteEditorEffect.RequestVoicePermission)
            }
            NoteEditorEvent.VoiceInputStopClicked -> {
                viewModelScope.launch {
                    voiceRecognizer.stop()
                }
            }
            NoteEditorEvent.VoicePermissionGranted -> {
                Log.d("NoteEditorVM", "start voice recognizer")
                voiceRecognizer.start()
            }
            NoteEditorEvent.VoicePermissionDenied -> {
                emitEffect(
                    NoteEditorEffect.ShowError("Для распознавания речи необходимо предоставить разрешение на запись аудио"),
                )
            }
            is NoteEditorEvent.VoiceTextRecognized -> {
                appendRecognizedText(event.text)
            }
            NoteEditorEvent.SaveClicked -> {
                saveNote()
            }
            is NoteEditorEvent.BackClicked -> {
                viewModelScope.launch {
                    currentTempImageUri?.let { uri ->
                        deleteTempImageUseCase(uri)
                    }

                    currentTempImageUri = null

                    emitEffect(NoteEditorEffect.NavigateBack)
                }
            }
        }
    }

    private fun observeVoiceState() {
        viewModelScope.launch {
            voiceRecognizer.state.collect { state ->
                _uiState.update {
                    it.copy(
                        voiceRecordingState = state,
                        voiceError = when (state) {
                            is VoiceState.Error -> when (state.error) {
                                VoiceError.RecordingFailed -> "Не удалось начать запись"
                                VoiceError.RecognitionFailed -> "Не удалось распознать речь"
                                VoiceError.Network -> "Проблема с сетью"
                                VoiceError.Unauthorized -> "Неверный токен или API-ключ распознавания речи"
                                VoiceError.EmptyResult -> "Результат распознавания пустой"
                                VoiceError.Unknown -> "Неизвестная ошибка распознавания речи"
                            }
                            else -> null
                        },
                    )
                }

                if (state is VoiceState.Success) {
                    val text = state.text.trim()
                    if (text.isNotBlank()) {
                        appendRecognizedText(text)
                    }
                    voiceRecognizer.cancel()
                }
            }
        }
    }

    private fun appendRecognizedText(text: String) {
        _uiState.update { current ->
            val separator = if (current.content.isBlank()) "" else "\n"
            current.copy(content = current.content + separator + text.trim())
        }
    }

    fun onVoicePermissionResult(granted: Boolean) {
        if (granted) {
            voiceRecognizer.start()
        } else {
            emitEffect(
                NoteEditorEffect.ShowError(
                    "Для распознавания речи необходимо предоставить разрешение на запись аудио",
                ),
            )
        }
    }

    fun stopVoiceInput() {
        viewModelScope.launch {
            voiceRecognizer.stop()
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
                    NoteEditorEffect.ShowError("Не удалось сохранить заметку")
                )
            }
        }
    }

    private fun deleteTempImage(uri: Uri) {
        viewModelScope.launch {
            deleteTempImageUseCase(uri)
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
