package com.core.domain.usercase.di.search

import com.core.dataapi.repository.SearchRepository
import com.core.domain.usercase.search.GetRecentListUseCase
import com.core.domain.usercase.search.GetSearchPostCountUseCase
import com.core.domain.usercase.search.GetSearchPostsUseCase
import com.core.domain.usercase.search.PostRecentListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SearchUseCaseModule {

    @Provides
    @Singleton
    fun provideGetRecentListUseCase(repository: SearchRepository) = GetRecentListUseCase(repository)

    @Provides
    @Singleton
    fun providePostRecentListUseCase(repository: SearchRepository) = PostRecentListUseCase(repository)

    @Provides
    @Singleton
    fun provideGetSearchPostCountUseCase(repository: SearchRepository) = GetSearchPostCountUseCase(repository)

    @Provides
    @Singleton
    fun provideGetSearchPostsUseCase(repository: SearchRepository) = GetSearchPostsUseCase(repository)
}
