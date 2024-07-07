package com.core.domain.usercase.di

import com.core.dataapi.repository.DataStoreRepository
import com.core.dataapi.repository.LoginRepository
import com.core.domain.usercase.CheckTokenUseCase
import com.core.domain.usercase.PostLoginUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun providePostLoginUseCase(repository: LoginRepository) = PostLoginUseCase(repository)

    @Provides
    @Singleton
    fun provideCheckTokenUseCase(repository: DataStoreRepository) = CheckTokenUseCase(repository)
}
