package com.core.domain.usercase.di.post

import com.core.dataapi.repository.CommunityRepository
import com.core.domain.usercase.post.GetPopularPostsUseCase
import com.core.domain.usercase.post.GetPostDetailCommentUseCase
import com.core.domain.usercase.post.GetPostDetailUseCase
import com.core.domain.usercase.post.GetPostScrapUseCase
import com.core.domain.usercase.post.GetPostsUseCase
import com.core.domain.usercase.post.PatchCommentUseCase
import com.core.domain.usercase.post.PostPostAddCommentUseCase
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

    @Provides
    @Singleton
    fun provideGetPostDetailUseCase(repository: CommunityRepository) = GetPostDetailUseCase(repository)

    @Provides
    @Singleton
    fun provideGetPostDetailCommentUseCase(repository: CommunityRepository) = GetPostDetailCommentUseCase(repository)

    @Provides
    @Singleton
    fun provideGetPostScrapUseCase(repository: CommunityRepository) = GetPostScrapUseCase(repository)

    @Provides
    @Singleton
    fun providePostPostAddCommentUseCase(repository: CommunityRepository) = PostPostAddCommentUseCase(repository)

    @Provides
    @Singleton
    fun providePatchCommentUseCase(repository: CommunityRepository) = PatchCommentUseCase(repository)
}
