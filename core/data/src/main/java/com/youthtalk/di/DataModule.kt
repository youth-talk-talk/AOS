package com.youthtalk.di

import com.core.dataapi.repository.LoginRepository
import com.youthtalk.repository.LoginRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindsLoginRepository(repository: LoginRepositoryImpl): LoginRepository
}
