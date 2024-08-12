package com.core.domain.usercase.di.policydetail

import com.core.dataapi.repository.CommentRepository
import com.core.dataapi.repository.PolicyDetailRepository
import com.core.domain.usercase.policydetail.GetPolicyDetailCommentUseCase
import com.core.domain.usercase.policydetail.GetPolicyDetailUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PolicyDetailUseCaseModule {

    @Provides
    @Singleton
    fun provideGetPolicyDetailUseCase(repository: PolicyDetailRepository) = GetPolicyDetailUseCase(repository)

    @Provides
    @Singleton
    fun provideGetPolicyDetailCommentUseCaseUseCase(repository: CommentRepository) = GetPolicyDetailCommentUseCase(repository)
}
