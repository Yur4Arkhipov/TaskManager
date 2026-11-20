package com.jacqulin.taskmanager.feature.settings.presentation.model

import androidx.annotation.StringRes

sealed interface SettingsEffect {

    data class ShowSuccess(
        @StringRes val messageResId: Int
    ) : SettingsEffect

    data class ShowError(
        @StringRes val messageResId: Int
    ) : SettingsEffect
}
