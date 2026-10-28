package com.jacqulin.taskmanager.feature.home.presentation.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.jacqulin.taskmanager.feature.home.presentation.model.HomeEvent

@Composable
fun CommitDayPlanOrderOnStop(onEvent: (HomeEvent) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnEvent = rememberUpdatedState(onEvent)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                currentOnEvent.value(HomeEvent.CommitDayPlanOrder)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}
