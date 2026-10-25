package com.jacqulin.taskmanager.feature.settings.presentation.tags

import com.jacqulin.taskmanager.core.designsystem.model.TagsSortType

data class TagsState(
    val searchQuery: String = "",
    val createQuery: String = "",
    val sortType: TagsSortType = TagsSortType.BY_NAME,
)
