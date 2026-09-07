package com.jacqulin.taskmanager.feature.notes.presentation.notebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacqulin.taskmanager.designsystem.model.SortType
import com.jacqulin.taskmanager.feature.notes.domain.usecase.DeleteNoteUseCase
import com.jacqulin.taskmanager.feature.notes.domain.usecase.ObserveNotesUseCase
import com.jacqulin.taskmanager.feature.notes.presentation.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesScreenViewModel @Inject constructor(
    observeNotesUseCase: ObserveNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    private val searchQueryInput = MutableStateFlow("")
    private val appliedSearchQuery = MutableStateFlow("")
    private val sortType = MutableStateFlow(SortType.NEW_TO_OLD)
    private val isDeleteModeEnabled = MutableStateFlow(false)

    private val _effects = MutableSharedFlow<NotesEffect>()
    val effects: SharedFlow<NotesEffect> = _effects.asSharedFlow()

    val uiState: StateFlow<NotesUiState> =
        combine(
            observeNotesUseCase(),
            searchQueryInput,
            appliedSearchQuery,
            sortType,
            isDeleteModeEnabled
        ) { notes, inputQuery, appliedQuery, sortType, isDeleteModeEnabled  ->

            val visibleNotes = notes
                .map { it.toUiModel() }
                .filter { note ->
                    val query = appliedQuery.trim()
                    query.isBlank() ||
                        note.title.contains(query, ignoreCase = true)
                }
                .let { notes ->
                    when (sortType) {
                        SortType.NEW_TO_OLD ->
                            notes.sortedByDescending { it.createdAtMillis }
                        SortType.OLD_TO_NEW ->
                            notes.sortedBy { it.createdAtMillis }
                    }
                }

            NotesUiState(
                searchQueryInput = inputQuery,
                appliedSearchQuery = appliedQuery,
                sortType = sortType,
                visibleNotes = visibleNotes,
                isEmpty = visibleNotes.isEmpty(),
                isDeleteModeEnabled = isDeleteModeEnabled
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            NotesUiState()
        )

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.OnSearchQueryChanged -> {
                searchQueryInput.value = event.query
            }
            NotesEvent.OnSearchSubmitted -> {
                appliedSearchQuery.value = searchQueryInput.value
            }
            is NotesEvent.OnSortChanged -> {
                sortType.value = event.sortType
            }
            NotesEvent.OnDeleteModeToggled -> {
                isDeleteModeEnabled.update { !it }
            }
            is NotesEvent.OnDeleteNoteClicked -> {
                deleteNote(event.noteId)
            }
            is NotesEvent.OnNoteClicked -> {
                handleNoteClick(event.noteId)
            }
            NotesEvent.OnCreateNoteClicked -> {
                if (isDeleteModeEnabled.value) return
                emitEffect(NotesEffect.NavigateToCreateNote)
            }
        }
    }

    private fun deleteNote(noteId: Int) {
        viewModelScope.launch {
            deleteNoteUseCase(noteId)
        }
    }

    private fun handleNoteClick(noteId: Int) {
        if (isDeleteModeEnabled.value) return
        emitEffect(NotesEffect.NavigateToExistingNote(noteId))
    }

    private fun emitEffect(effect: NotesEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
