package com.jacqulin.taskmanager.feature.settings.presentation.model

import com.jacqulin.taskmanager.core.model.Tag

data class TagManagementItem(
    val tag: Tag,
    val taskCount: Int
)
