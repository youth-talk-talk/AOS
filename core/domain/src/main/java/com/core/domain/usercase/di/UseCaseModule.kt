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
import com.core.domain.usercase.PostLoginUseCase
import com.core.domain.usercase.PostPolicyCommentUseCase
import com.core.domain.usercase.PostPolicyDeleteCommentUseCase
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
    fun provideSetCategoriesUseCase(userRepository: UserRepository, homeRepository: HomeRepository) =
        ChangeCategoriesUseCase(userRepository, homeRepository)

    @Provides
    @Singleton
    fun providePostPolicyScrapUseCase(repository: SpecPolicyRepository) = PostPolicyScrapUseCase(repository)

    @Provides
    @Singleton
    fun providePostPolicyCommentUseCase(repository: SpecPolicyRepository) = PostPolicyCommentUseCase(repository)

    @Provides
    @Singleton
    fun providePostPolicyDeleteCommentUseCase(repository: SpecPolicyRepository) = PostPolicyDeleteCommentUseCase(repository)

    @Provides
    @Singleton
    fun providePostPostScrapUseCase(repository: CommunityRepository) = PostPostScrapUseCase(repository)
}
