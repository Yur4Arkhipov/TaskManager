package com.jacqulin.taskmanager.feature.home.presentation.model

data class HomeTaskUi(
    val id: Int,
    val title: String,
    val durationMinutes: Int? = null,
    val isCompleted: Boolean = false,
) {
    val hasDuration: Boolean get() = durationMinutes != null
}
