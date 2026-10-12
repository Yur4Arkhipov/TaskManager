package com.jacqulin.taskmanager.feature.notes.presentation.noteeditor

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacqulin.taskmanager.core.domain.notes.CreateTempImageUseCase
import com.jacqulin.taskmanager.core.domain.notes.DeleteTempImageUseCase
import com.jacqulin.taskmanager.core.domain.notes.GetNoteByIdUseCase
import com.jacqulin.taskmanager.core.domain.notes.SaveNoteUseCase
import com.jacqulin.taskmanager.core.domain.voice.VoiceInputUseCase
import com.jacqulin.taskmanager.core.model.Note
import com.jacqulin.taskmanager.core.model.voice.SpeechRecognitionEvent
import com.jacqulin.taskmanager.core.model.voice.SpeechRecognitionFailure
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class NoteEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getNoteUseCase: GetNoteByIdUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
    private val createTempImageUseCase: CreateTempImageUseCase,
    private val deleteTempImageUseCase: DeleteTempImageUseCase,
    private val voiceInputUseCase: VoiceInputUseCase,
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
                log("Voice input clicked")
                emitEffect(NoteEditorEffect.RequestVoicePermission)
            }
            NoteEditorEvent.VoiceInputStopClicked -> {
                log("Stop voice input clicked")
                setVoiceState(VoiceState.Processing)
                viewModelScope.launch {
                    voiceInputUseCase.stop()
                        .onSuccess { text ->
                            if (text.isNotBlank()) {
                                appendRecognizedText(text)
                                setVoiceState(VoiceState.Success(text))
                            }
                        }
                        .onFailure {
                            setVoiceState(VoiceState.Error(VoiceError.RecognitionFailed))
                        }
                }
            }
            NoteEditorEvent.VoicePermissionGranted -> {
                log("Voice permission granted")
                startSpeechRecognition()
            }
            NoteEditorEvent.VoicePermissionDenied -> {
                log("Voice permission denied")
                emitEffect(
                    NoteEditorEffect.ShowError("Для распознавания речи необходимо предоставить разрешение на запись аудио")
                )
            }
            NoteEditorEvent.VoiceInputCancel -> {
                log("Cancel voice input clicked")
                viewModelScope.launch {
                    voiceInputUseCase.cancel()
                    setVoiceState(VoiceState.Idle)
                }
            }
            NoteEditorEvent.VoiceInputRetry -> {
                log("Retry voice input clicked")
                startSpeechRecognition()
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

    private fun startSpeechRecognition() {
        setVoiceState(VoiceState.Recording())

        viewModelScope.launch {
            voiceInputUseCase.start()
                .onFailure {
                    setVoiceState(VoiceState.Error(VoiceError.RecordingFailed))
                    return@launch
                }
            voiceInputUseCase.listen().collect { event ->
                when (event) {
                    SpeechRecognitionEvent.Ready,
                    SpeechRecognitionEvent.SpeechStarted -> {
                        if (_uiState.value.voiceState !is VoiceState.Recording) {
                            setVoiceState(VoiceState.Recording())
                        }
                    }

                    SpeechRecognitionEvent.SpeechEnded -> {
                        setVoiceState(VoiceState.Processing)
                    }

                    is SpeechRecognitionEvent.PartialResult -> {
                        setVoiceState(VoiceState.Recording(event.text))
                    }

                    is SpeechRecognitionEvent.Result -> {
                        appendRecognizedText(event.text)
                        setVoiceState(VoiceState.Success(event.text))
                        delay(3000.milliseconds)
                        setVoiceState(VoiceState.Idle)
                    }

                    is SpeechRecognitionEvent.Error -> {
                        setVoiceState(VoiceState.Error(event.reason.toVoiceError()))
                    }
                }
            }
        }
    }

    private fun setVoiceState(state: VoiceState) {
        _uiState.update {
            it.copy(voiceState = state)
        }
    }

    private fun SpeechRecognitionFailure.toVoiceError(): VoiceError = when (this) {
        SpeechRecognitionFailure.ServiceUnavailable,
        SpeechRecognitionFailure.ServiceBusy,
        SpeechRecognitionFailure.TooManyRequests,
        SpeechRecognitionFailure.UnsupportedLanguage -> VoiceError.RecognitionFailed
        SpeechRecognitionFailure.NoMatch -> VoiceError.EmptyResult
        SpeechRecognitionFailure.Network -> VoiceError.Network
        SpeechRecognitionFailure.AudioInput,
        SpeechRecognitionFailure.PermissionDenied -> VoiceError.RecordingFailed
        SpeechRecognitionFailure.Unknown -> VoiceError.Unknown
    }

    private fun appendRecognizedText(text: String) {
        _uiState.update { current ->
            val separator = if (current.content.isBlank()) "" else "\n"
            current.copy(content = current.content + separator + text.trim())
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

    private fun log(message: String) {
        Log.d(TAG, "${System.currentTimeMillis()} $message")
    }

    private companion object {
        const val TAG = "NoteEditorVM"
    }
}
