package com.jacqulin.taskmanager.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jacqulin.taskmanager.core.model.FocusMode
import com.jacqulin.taskmanager.core.model.FocusSession
import com.jacqulin.taskmanager.core.model.FocusSessionStatus
import com.jacqulin.taskmanager.core.model.Note

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val taskId: Int?,
    val taskTitle: String,
    val mode: String,
    val workDurationMinutes: Int?,
    val breakDurationMinutes: Int?,
    val longBreakDurationMinutes: Int?,
    val cyclesCount: Int?,
    val status: String,
    val currentCycle: Int,
    val isBreak: Boolean,
    val startedAtMillis: Long?,
    val pausedAtMillis: Long?,
)

fun FocusSessionEntity.asExternalModel(): FocusSession =
    FocusSession(
        id = id,
        taskId = taskId ?: 0,
        taskTitle = taskTitle,
        mode = mode.toFocusMode(),
        workDurationMinutes = workDurationMinutes,
        breakDurationMinutes = breakDurationMinutes,
        longBreakDurationMinutes = longBreakDurationMinutes,
        cyclesCount = cyclesCount,
        status = status.toFocusSessionStatus(),
        currentCycle = currentCycle,
        isBreak = isBreak,
        startedAtMillis = startedAtMillis,
        pausedAtMillis = pausedAtMillis,
    )

fun FocusSession.toEntity(): FocusSessionEntity = FocusSessionEntity(
    id = id,
    taskId = taskId,
    taskTitle = taskTitle,
    mode = mode.name,
    workDurationMinutes = workDurationMinutes,
    breakDurationMinutes = breakDurationMinutes,
    longBreakDurationMinutes = longBreakDurationMinutes,
    cyclesCount = cyclesCount,
    status = status.name,
    currentCycle = currentCycle,
    isBreak = isBreak,
    startedAtMillis = startedAtMillis,
    pausedAtMillis = pausedAtMillis
)

fun String.toFocusMode(): FocusMode =
    FocusMode.entries.first { it.name == this }

fun String.toFocusSessionStatus(): FocusSessionStatus =
    FocusSessionStatus.entries.first { it.name == this }
