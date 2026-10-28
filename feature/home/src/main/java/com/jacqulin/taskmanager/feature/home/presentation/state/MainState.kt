package com.jacqulin.taskmanager.feature.home.presentation.state

import com.jacqulin.taskmanager.feature.home.presentation.model.HomeTaskUi

data class MainState(
    val dateLabel: String = "",
    val tasks: List<HomeTaskUi> = emptyList(),
//    val isFullPlanShown: Boolean = false,
//    val isBottomSheetVisible: Boolean = false,
)
