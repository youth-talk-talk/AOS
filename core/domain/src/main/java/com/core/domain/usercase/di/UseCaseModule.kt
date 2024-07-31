package com.core.domain.usercase.di

import com.core.dataapi.repository.LoginRepository
import com.core.dataapi.repository.UserRepository
import com.core.domain.usercase.CheckTokenUseCase
import com.core.domain.usercase.GetCategoriesUseCase
import com.core.domain.usercase.GetUserUserCase
import com.core.domain.usercase.PostLoginUseCase
import com.core.domain.usercase.PostSignUseCase
import com.core.domain.usercase.SetCategoriesUseCase
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
    fun provideGetUserUseCase(repository: UserRepository) = GetUserUserCase(repository)

    @Provides
    @Singleton
    fun provideGetCategoriesUseCase(repository: UserRepository) = GetCategoriesUseCase(repository)

    @Provides
    @Singleton
    fun provideSetCategoriesUseCase(repository: UserRepository) = SetCategoriesUseCase(repository)
}
