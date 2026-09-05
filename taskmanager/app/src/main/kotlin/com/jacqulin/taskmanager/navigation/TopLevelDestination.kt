package com.jacqulin.taskmanager.navigation

import androidx.annotation.StringRes
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.designsystem.icon.AppIcons
import com.jacqulin.taskmanager.feature.notes.navigation.NotesBaseRoute
import com.jacqulin.taskmanager.feature.notes.navigation.NotesRoute
import com.jacqulin.taskmanager.feature.settings.navigation.SettingsBaseRoute
import com.jacqulin.taskmanager.feature.settings.navigation.SettingsRoute
import com.jacqulin.taskmanager.feature.tasks.navigation.TasksBaseRoute
import com.jacqulin.taskmanager.feature.tasks.navigation.TasksRoute
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val icon: Int,
    @StringRes val titleTextId: Int,
    val route: KClass<*>,
    val baseRoute: KClass<*> = route
) {
    TASKS(
        icon = AppIcons.Tasks,
        titleTextId = R.string.tasks_title,
        route = TasksRoute::class,
        baseRoute = TasksBaseRoute::class
    ),
    NOTES(
        icon = AppIcons.Notes,
        titleTextId = R.string.notes_title,
        route = NotesRoute::class,
        baseRoute = NotesBaseRoute::class
    ),
    SETTINGS(
        icon = AppIcons.Settings,
        titleTextId = R.string.settings_title,
        route = SettingsRoute::class,
        baseRoute = SettingsBaseRoute::class
    )
}
