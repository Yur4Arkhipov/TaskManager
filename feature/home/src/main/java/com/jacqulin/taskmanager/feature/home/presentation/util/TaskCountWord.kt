package com.jacqulin.taskmanager.feature.home.presentation.util

fun taskCountWord(count: Int): String = when {
    count % 10 == 1 && count % 100 != 11 -> "задача"
    count % 10 in 2..4 && count % 100 !in 12..14 -> "задачи"
    else -> "задач"
}
