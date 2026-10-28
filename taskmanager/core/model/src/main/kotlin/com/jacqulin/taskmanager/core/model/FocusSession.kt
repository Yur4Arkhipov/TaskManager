package com.jacqulin.taskmanager.core.model

data class FocusSession(
    val id: Long,
    val taskId: Int,
    val taskTitle: String,
    val mode: FocusMode,
    val workDurationMinutes: Int?,
    val breakDurationMinutes: Int?,
    val longBreakDurationMinutes: Int?,
    val cyclesCount: Int?,
    val status: FocusSessionStatus,
    val currentCycle: Int,
    val isBreak: Boolean,
    val startedAtMillis: Long?,
    val pausedAtMillis: Long?,
)
