package com.jacqulin.taskmanager.feature.settings.presentation.tags

import com.jacqulin.taskmanager.core.designsystem.model.TagsSortType

sealed interface TagsEvent {
    data class OnSearchQueryChanged(val query: String) : TagsEvent
    data class OnCreateQueryChanged(val query: String) : TagsEvent
    data class OnCreateTag(val title: String) : TagsEvent
    data class OnRenameTag(val tagId: Int, val title: String) : TagsEvent
    data class OnDeleteTag(val tagId: Int) : TagsEvent
    data class OnSortChanged(val sortType: TagsSortType) : TagsEvent
}
