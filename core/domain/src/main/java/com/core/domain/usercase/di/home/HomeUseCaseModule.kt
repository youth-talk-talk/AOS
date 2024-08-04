package com.core.domain.usercase.di.home

import com.core.dataapi.repository.HomeRepository
import com.core.domain.usercase.home.GetAllPoliciesUseCase
import com.core.domain.usercase.home.GetPopularPoliciesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeUseCaseModule {

    @Provides
    @Singleton
    fun provideGetAllPolicesUseCase(repository: HomeRepository) = GetAllPoliciesUseCase(repository)

    @Provides
    @Singleton
    fun provideGetPopularPolicesUseCase(repository: HomeRepository) = GetPopularPoliciesUseCase(repository)
}
