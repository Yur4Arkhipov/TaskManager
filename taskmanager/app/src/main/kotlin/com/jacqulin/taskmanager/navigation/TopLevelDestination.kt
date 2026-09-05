package com.jacqulin.taskmanager.navigation

import androidx.annotation.StringRes
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.designsystem.icon.AppIcons
import com.jacqulin.taskmanager.feature.notes.navigation.NotesBaseRoute
import com.jacqulin.taskmanager.feature.notes.navigation.NotesRoute
import kotlin.reflect.KClass

//data class TopLevelNavItem(
//    val icon: Int,
//    @StringRes val titleTextId: Int
//)
//
//val NOTES = TopLevelNavItem(
//    icon = AppIcons.Notes,
//    titleTextId = R.string.notes_title
//)
//
//val TASKS = TopLevelNavItem(
//    icon = AppIcons.Tasks,
//    titleTextId = R.string.tasks_title
//)
//
//val SETTINGS = TopLevelNavItem(
//    icon = AppIcons.Settings,
//    titleTextId = R.string.settings_title
//)

enum class TopLevelDestination(
    val icon: Int,
    @StringRes val titleTextId: Int,
    val route: KClass<*>,
    val baseRoute: KClass<*> = route
) {
    NOTES(
        icon = AppIcons.Notes,
        titleTextId = R.string.notes_title,
        route = NotesRoute::class,
        baseRoute = NotesBaseRoute::class
    ),
    TASKS(
        icon = AppIcons.Tasks,
        titleTextId = R.string.tasks_title,
        route = Any::class
    ),
    SETTINGS(
        icon = AppIcons.Settings,
        titleTextId = R.string.settings_title,
        route = Any::class
    )
}
