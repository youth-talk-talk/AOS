package com.core.domain.usercase.di.specpolicy

import com.core.dataapi.repository.SpecPolicyRepository
import com.core.domain.usercase.specpolicy.GetPolicyCountUseCase
import com.core.domain.usercase.specpolicy.GetUserFilterInfoUseCase
import com.core.domain.usercase.specpolicy.PostSpecPoliciesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SpecPolicyUseCaseModule {
    @Provides
    @Singleton
    fun providePostSpecPoliciesUseCase(repository: SpecPolicyRepository) = PostSpecPoliciesUseCase(repository)

    @Provides
    @Singleton
    fun provideGetPolicyCountUseCase(repository: SpecPolicyRepository) = GetPolicyCountUseCase(repository)

    @Provides
    @Singleton
    fun provideGetUserFilterInfoUseCase(repository: SpecPolicyRepository) = GetUserFilterInfoUseCase(repository)
}
