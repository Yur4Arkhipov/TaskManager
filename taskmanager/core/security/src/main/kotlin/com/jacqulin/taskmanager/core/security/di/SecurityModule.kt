package com.jacqulin.taskmanager.core.security.di

import com.jacqulin.taskmanager.core.security.AndroidKeystoreTokenStorage
import com.jacqulin.taskmanager.core.security.SecureTokenStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SecurityModule {
    @Binds
    @Singleton
    abstract fun bindSecureTokenStore(
        impl: AndroidKeystoreTokenStorage
    ): SecureTokenStorage
}
