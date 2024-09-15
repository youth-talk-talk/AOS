package com.core.domain.usercase.di

import com.core.dataapi.repository.CommunityRepository
import com.core.dataapi.repository.HomeRepository
import com.core.dataapi.repository.LoginRepository
import com.core.dataapi.repository.SpecPolicyRepository
import com.core.dataapi.repository.UserRepository
import com.core.domain.usercase.ChangeCategoriesUseCase
import com.core.domain.usercase.CheckTokenUseCase
import com.core.domain.usercase.GetCategoriesUseCase
import com.core.domain.usercase.GetUserUseCase
import com.core.domain.usercase.PostCommentLikeUseCase
import com.core.domain.usercase.PostDeleteCommentUseCase
import com.core.domain.usercase.PostLoginUseCase
import com.core.domain.usercase.PostPolicyAddCommentUseCase
import com.core.domain.usercase.PostPolicyScrapUseCase
import com.core.domain.usercase.PostPostScrapUseCase
import com.core.domain.usercase.PostSignUseCase
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
    fun provideCheckTokenUseCase(repository: LoginRepository) = CheckTokenUseCase(repository)

    @Provides
    @Singleton
    fun provideSignUpUseCase(repository: LoginRepository) = PostSignUseCase(repository)

    @Provides
    @Singleton
    fun provideGetUserUseCase(repository: UserRepository) = GetUserUseCase(repository)

    @Provides
    @Singleton
    fun provideGetCategoriesUseCase(repository: UserRepository) = GetCategoriesUseCase(repository)

    @Provides
    @Singleton
    fun provideSetCategoriesUseCase(userRepository: UserRepository) = ChangeCategoriesUseCase(userRepository)

    @Provides
    @Singleton
    fun providePostPolicyScrapUseCase(repository: SpecPolicyRepository, homeRepository: HomeRepository) =
        PostPolicyScrapUseCase(repository, homeRepository)

    @Provides
    @Singleton
    fun providePostPolicyAddCommentUseCase(repository: SpecPolicyRepository) = PostPolicyAddCommentUseCase(repository)

    @Provides
    @Singleton
    fun providePostDeleteCommentUseCase(repository: SpecPolicyRepository) = PostDeleteCommentUseCase(repository)

    @Provides
    @Singleton
    fun providePostPostScrapUseCase(repository: CommunityRepository) = PostPostScrapUseCase(repository)

    @Provides
    @Singleton
    fun providePostCommentLikeUseCase(repository: CommunityRepository) = PostCommentLikeUseCase(repository)
}
