package com.jacqulin.taskmanager.core.network.di

import com.jacqulin.taskmanager.core.network.NetworkDataSource
import com.jacqulin.taskmanager.core.network.NetworkDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkDataSourceModule {

    @Binds
    @Singleton
    internal abstract fun bindsNetworkDataSource(
        impl: NetworkDataSourceImpl,
    ): NetworkDataSource
}
