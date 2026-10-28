package com.jacqulin.taskmanager.feature.settings.presentation.tags

import com.jacqulin.taskmanager.feature.settings.presentation.model.TagManagementItem

data class TagsUiState(
    val tagsState: TagsState = TagsState(),
    val visibleTags: List<TagManagementItem> = emptyList()
)
