package com.jacqulin.taskmanager.feature.settings.presentation.tags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacqulin.taskmanager.core.designsystem.model.TagsSortType
import com.jacqulin.taskmanager.core.domain.tags.CreateTagUseCase
import com.jacqulin.taskmanager.core.domain.tags.DeleteTagUseCase
import com.jacqulin.taskmanager.core.domain.tags.ObserveTagsUseCase
import com.jacqulin.taskmanager.core.domain.tags.RenameTagUseCase
import com.jacqulin.taskmanager.core.domain.tasks.ObserveTasksUseCase
import com.jacqulin.taskmanager.feature.settings.presentation.model.TagManagementItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagsViewModel @Inject constructor(
    private val createTagUseCase: CreateTagUseCase,
    private val renameTagUseCase: RenameTagUseCase,
    private val deleteTagUseCase: DeleteTagUseCase,
    observeTasks: ObserveTasksUseCase,
    observeTags: ObserveTagsUseCase
) : ViewModel() {

    private val tagsState = MutableStateFlow(TagsState())

    val uiState: StateFlow<TagsUiState> =
        combine(
        observeTags(),
        observeTasks(),
        tagsState
    ) { allTags, tasks, tagsState ->
        val visibleTags = allTags
            .map { tag ->
                TagManagementItem(
                    tag = tag,
                    taskCount = tasks.count { task -> task.tags.any { it.id == tag.id } }
                )
            }
            .filter { item ->
                val query = tagsState.searchQuery.trim()
                query.isBlank() || item.tag.name.contains(query, ignoreCase = true)
            }
            .let { tags ->
                when (tagsState.sortType) {
                    TagsSortType.BY_NAME -> {
                        tags.sortedBy { item -> item.tag.name.lowercase() }
                    }
                    TagsSortType.BY_USAGE -> {
                        tags.sortedByDescending(TagManagementItem::taskCount)
                    }
                    else -> tags
                }
            }
            TagsUiState(
                tagsState = tagsState,
                visibleTags = visibleTags
            )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TagsUiState()
    )

    fun onEvent(event: TagsEvent) {
        when (event) {
            is TagsEvent.OnSearchQueryChanged -> {
                tagsState.update { it.copy(searchQuery = event.query) }
            }
            is TagsEvent.OnCreateQueryChanged -> {
                tagsState.update { it.copy(createQuery = event.query) }
            }
            is TagsEvent.OnCreateTag -> {
                tagsState.update { it.copy(createQuery = "") }
                viewModelScope.launch { createTagUseCase(event.title) }
            }
            is TagsEvent.OnRenameTag -> {
                viewModelScope.launch { renameTagUseCase(event.tagId, event.title) }
            }
            is TagsEvent.OnDeleteTag -> {
                viewModelScope.launch { deleteTagUseCase(event.tagId) }
            }
            is TagsEvent.OnSortChanged -> {
                tagsState.update { it.copy(sortType = event.sortType) }
            }
        }
    }
}
