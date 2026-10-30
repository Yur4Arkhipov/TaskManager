package com.jacqulin.taskmanager.feature.settings.presentation.model

data class TagsUiState(
    val tagsState: TagsState = TagsState(),
    val visibleTags: List<TagManagementItem> = emptyList()
)
