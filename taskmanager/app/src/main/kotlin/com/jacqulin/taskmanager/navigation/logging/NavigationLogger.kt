package com.jacqulin.taskmanager.navigation.logging

import android.util.Log
import androidx.savedstate.SavedState
import javax.inject.Inject

class NavigationLogger @Inject constructor() {
    fun logNavigation(
        fromRoute: String?,
        toRoute: String?,
        arguments: SavedState?,
    ) {
        Log.d(
            "NavLogger",
            """
                navigate:
                from=$fromRoute
                to=$toRoute
                args=$arguments
            """.trimIndent(),
        )
    }
}
