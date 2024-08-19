package com.core.domain.usercase.di.post

import com.core.dataapi.repository.CommunityRepository
import com.core.domain.usercase.post.GetPopularPostsUseCase
import com.core.domain.usercase.post.GetPostsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PostUseCaseModule {

    @Provides
    @Singleton
    fun provideGetPopularPostsUseCase(repository: CommunityRepository) = GetPopularPostsUseCase(repository)

    @Provides
    @Singleton
    fun provideGetPostsUseCase(repository: CommunityRepository) = GetPostsUseCase(repository)
}
