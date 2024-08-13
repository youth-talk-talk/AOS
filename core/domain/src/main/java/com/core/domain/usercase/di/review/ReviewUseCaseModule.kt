package com.core.domain.usercase.di.review

import com.core.dataapi.repository.CommunityRepository
import com.core.dataapi.repository.UserRepository
import com.core.domain.usercase.review.GetReviewCategoriesUseCase
import com.core.domain.usercase.review.PostPopularReviewPostsUseCase
import com.core.domain.usercase.review.PostReviewPostsUseCase
import com.core.domain.usercase.review.SetReviewCategoriesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReviewUseCaseModule {

    @Provides
    @Singleton
    fun provideGetReviewCategoriesUseCase(repository: UserRepository) = GetReviewCategoriesUseCase(repository)

    @Provides
    @Singleton
    fun provideSetReviewCategoriesUseCase(repository: UserRepository) = SetReviewCategoriesUseCase(repository)

    @Provides
    @Singleton
    fun providePostPopularReviewPostsUseCase(repository: CommunityRepository) = PostPopularReviewPostsUseCase(repository)

    @Provides
    @Singleton
    fun providePostReviewPostsUseCase(repository: CommunityRepository) = PostReviewPostsUseCase(repository)
}
