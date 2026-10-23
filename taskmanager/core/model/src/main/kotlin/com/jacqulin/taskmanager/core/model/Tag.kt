package com.jacqulin.taskmanager.core.model

data class Tag(
    val id: Int,
    val name: String,
    val color: Long = 0xFFE59A00.toInt().toLong(),
)
