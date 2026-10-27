package com.jacqulin.taskmanager.feature.home.presentation.model

data class FocusSession(
    val id: String,
    val taskId: Int,
    val status: FocusSessionStatus,
    val durationMinutes: Int,
    val remainingSeconds: Int,
    val startedAtMillis: Long?
)
