package com.jacqulin.taskmanager.feature.home.presentation.model

data class MainState(
    val dateLabel: String = "",
    val tasks: List<HomeTaskUi> = emptyList(),
//    val isFullPlanShown: Boolean = false,
//    val isBottomSheetVisible: Boolean = false,
)
