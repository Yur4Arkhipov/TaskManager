package com.jacqulin.taskmanager.feature.settings.presentation.tags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacqulin.taskmanager.core.domain.tags.ManageTagsUseCases
import com.jacqulin.taskmanager.core.domain.tasks.ObserveTasksUseCase
import com.jacqulin.taskmanager.feature.settings.presentation.model.TagManagementItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagsViewModel @Inject constructor(
    private val tags: ManageTagsUseCases,
    observeTasks: ObserveTasksUseCase,
) : ViewModel() {
    val items = combine(
        tags.observeTags(),
        observeTasks()
    ) { allTags, tasks ->
        allTags.map { tag ->
            TagManagementItem(
                tag = tag,
                taskCount = tasks.count { task -> task.tags.any { it.id == tag.id } }
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun create(name: String) = viewModelScope.launch { tags.create(name) }
    fun rename(tagId: Int, name: String) = viewModelScope.launch { tags.rename(tagId, name) }
    fun delete(tagId: Int) = viewModelScope.launch { tags.delete(tagId) }
}
