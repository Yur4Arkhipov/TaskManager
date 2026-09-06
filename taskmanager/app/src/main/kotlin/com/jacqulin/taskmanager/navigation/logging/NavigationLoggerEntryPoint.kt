package com.jacqulin.taskmanager.navigation.logging

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface NavigationLoggerEntryPoint {
    fun navigationLogger(): NavigationLogger
}
