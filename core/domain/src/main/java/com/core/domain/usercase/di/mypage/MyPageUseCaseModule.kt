package com.core.domain.usercase.di.mypage

import com.core.dataapi.repository.AnnounceRepository
import com.core.dataapi.repository.MyPageRepository
import com.core.domain.usercase.mypage.GetAnnounceDetailUseCase
import com.core.domain.usercase.mypage.GetAnnouncesUseCase
import com.core.domain.usercase.mypage.GetDeadlinePolicesUseCase
import com.core.domain.usercase.mypage.GetMyPageCommentsUseCase
import com.core.domain.usercase.mypage.GetMyPagePostsUseCase
import com.core.domain.usercase.mypage.GetScrapPoliciesUseCase
import com.core.domain.usercase.mypage.PostUserLogoutUseCase
import com.core.domain.usercase.mypage.PostUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MyPageUseCaseModule {
    @Provides
    @Singleton
    fun provideGetScrapPoliciesUseCase(repository: MyPageRepository) = GetScrapPoliciesUseCase(repository)

    @Provides
    @Singleton
    fun provideGetMyPagePostsUseCase(repository: MyPageRepository) = GetMyPagePostsUseCase(repository)

    @Provides
    @Singleton
    fun provideGetMyPageCommentsUseCase(repository: MyPageRepository) = GetMyPageCommentsUseCase(repository)

    @Provides
    @Singleton
    fun provideGetDeadlinePolicesUseCase(repository: MyPageRepository) = GetDeadlinePolicesUseCase(repository)

    @Provides
    @Singleton
    fun providePostUserUseCase(repository: MyPageRepository) = PostUserUseCase(repository)

    @Provides
    @Singleton
    fun providePostUserLogoutUseCase(repository: MyPageRepository) = PostUserLogoutUseCase(repository)

    @Provides
    @Singleton
    fun provideGetAnnouncesUseCase(repository: AnnounceRepository) = GetAnnouncesUseCase(repository)

    @Provides
    @Singleton
    fun provideGetAnnounceDetailUseCase(repository: AnnounceRepository) = GetAnnounceDetailUseCase(repository)
}
