package com.jacqulin.taskmanager.feature.notes.presentation.notebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacqulin.taskmanager.feature.notes.domain.usecase.GetNotesUseCase
import com.jacqulin.taskmanager.feature.notes.presentation.mappers.toUiModel
import com.jacqulin.taskmanager.feature.notes.presentation.model.NoteListItemUi
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
class NotesScreenViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase
) : ViewModel() {

    private val allNotes = MutableStateFlow<List<NoteListItemUi>>(emptyList())
    private val _uiState = MutableStateFlow(NotesUiState())
    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<NotesEffect>()
    val effects: SharedFlow<NotesEffect> = _effects.asSharedFlow()

    init {
        viewModelScope.launch {
            getNotesUseCase().collect { notes ->
                setNotes(notes.map { it.toUiModel() })
            }
        }
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.OnSearchQueryChanged -> {
                _uiState.update { current ->
                    current.copy(searchQueryInput = event.query)
                }
            }

            NotesEvent.OnSearchSubmitted -> {
                _uiState.update { current ->
                    current.copy(appliedSearchQuery = current.searchQueryInput)
                }
                recomputeVisibleNotes()
            }

            is NotesEvent.OnSortChanged -> {
                _uiState.update { current -> current.copy(sortType = event.sortType) }
                recomputeVisibleNotes()
            }

            NotesEvent.OnDeleteModeToggled -> {
                _uiState.update { current -> current.copy(isDeleteModeEnabled = !current.isDeleteModeEnabled) }
            }

            is NotesEvent.OnDeleteNoteClicked -> {
                deleteNote(event.noteId)
            }

            is NotesEvent.OnNoteClicked -> {
                handleNoteClick(event.noteId)
            }

            NotesEvent.OnCreateNoteClicked -> {
                if (_uiState.value.isDeleteModeEnabled) return
                emitEffect(NotesEffect.NavigateToCreateNote)
            }
        }
    }

    fun setNotes(notes: List<NoteListItemUi>) {
        allNotes.value = notes
        recomputeVisibleNotes()
    }

    private fun deleteNote(noteId: String) {
        allNotes.update { notes -> notes.filterNot { it.id == noteId } }
        recomputeVisibleNotes()
    }

    private fun handleNoteClick(noteId: String) {
        if (_uiState.value.isDeleteModeEnabled) return
        emitEffect(NotesEffect.NavigateToExistingNote(noteId))
    }

    private fun recomputeVisibleNotes() {
        val currentState = _uiState.value
        val query = currentState.appliedSearchQuery.trim()

        val filteredNotes = allNotes.value
            .filter { note ->
                if (query.isBlank()) true else note.title.contains(query, ignoreCase = true)
            }
            .let { notes ->
                when (currentState.sortType) {
                    NotesSortType.NEW_TO_OLD -> notes.sortedByDescending { it.createdAtMillis }
                    NotesSortType.OLD_TO_NEW -> notes.sortedBy { it.createdAtMillis }
                }
            }

        _uiState.update { current ->
            current.copy(
                visibleNotes = filteredNotes,
                isEmpty = filteredNotes.isEmpty()
            )
        }
    }

    private fun emitEffect(effect: NotesEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
