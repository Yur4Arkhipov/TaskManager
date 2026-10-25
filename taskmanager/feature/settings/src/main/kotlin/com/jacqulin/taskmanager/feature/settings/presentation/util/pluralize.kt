package com.jacqulin.taskmanager.feature.settings.presentation.util

internal fun pluralize(
    count: Int,
    one: String,
    few: String,
    many: String,
): String {
    val mod100 = count % 100
    val mod10 = count % 10
    return when {
        mod100 in 11..14 -> many
        mod10 == 1     -> one
        mod10 in 2..4  -> few
        else -> many
    }
}
