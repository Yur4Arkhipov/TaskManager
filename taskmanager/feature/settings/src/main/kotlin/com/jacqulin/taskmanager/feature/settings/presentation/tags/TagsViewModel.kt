package com.jacqulin.taskmanager.feature.settings.presentation.tags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacqulin.taskmanager.core.domain.tasks.ManageTagsUseCases
import com.jacqulin.taskmanager.core.domain.tasks.ObserveTasksUseCase
import com.jacqulin.taskmanager.core.model.Tag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TagManagementItem(val tag: Tag, val taskCount: Int)

@HiltViewModel
class TagsViewModel @Inject constructor(
    private val tags: ManageTagsUseCases,
    observeTasks: ObserveTasksUseCase,
) : ViewModel() {
//    val items = combine(tags.observeTags(), observeTasks()) { allTags, tasks ->
//        allTags.map { tag ->
//            TagManagementItem(tag, tasks.count { task -> task.tags.any { it.id == tag.id } })
//        }
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val items = combine(mockObserveTags(), observeTasks()) { allTags, tasks ->
        allTags.map { tag ->
            TagManagementItem(tag, tasks.count { task -> task.tags.any { it.id == tag.id } })
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun create(name: String, color: Long) = viewModelScope.launch { tags.create(name, color) }
    fun rename(tagId: Int, name: String) = viewModelScope.launch { tags.rename(tagId, name) }
    fun delete(tagId: Int) = viewModelScope.launch { tags.delete(tagId) }

    private fun mockObserveTags(): Flow<List<Tag>> = flowOf(
        listOf(
            Tag(id = 1, name = "Работа",   color = 0xFF2196F3),
            Tag(id = 2, name = "Личное",   color = 0xFF4CAF50),
            Tag(id = 3, name = "Учёба",    color = 0xFFFF9800),
            Tag(id = 4, name = "Здоровье", color = 0xFFE91E63),
        )
    )
}
