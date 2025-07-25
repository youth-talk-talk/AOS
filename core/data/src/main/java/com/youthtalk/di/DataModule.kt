package com.youthtalk.di

import com.core.dataapi.repository.AnnounceRepository
import com.core.dataapi.repository.CommentRepository
import com.core.dataapi.repository.CommunityRepository
import com.core.dataapi.repository.HomeRepository
import com.core.dataapi.repository.LoginRepository
import com.core.dataapi.repository.PolicyRepository
import com.core.dataapi.repository.ReportRepository
import com.core.dataapi.repository.SearchRepository
import com.core.dataapi.repository.SpecPolicyRepository
import com.core.dataapi.repository.SseRepository
import com.core.dataapi.repository.UserRepository
import com.core.datastore.datasource.DataSource
import com.core.datastore.datasource.DataStoreDataSource
import com.youthtalk.repository.AnnounceRepositoryImpl
import com.youthtalk.repository.CommentRepositoryImpl
import com.youthtalk.repository.CommunityRepositoryImpl
import com.youthtalk.repository.HomeRepositoryImpl
import com.youthtalk.repository.LoginRepositoryImpl
import com.youthtalk.repository.PolicyRepositoryImpl
import com.youthtalk.repository.ReportRepositoryImpl
import com.youthtalk.repository.SearchRepositoryImpl
import com.youthtalk.repository.SpecPolicyRepositoryImpl
import com.youthtalk.repository.SseRepositoryImpl
import com.youthtalk.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindsDataSource(dataStoreDataSource: DataStoreDataSource): DataSource

    @Binds
    abstract fun bindsUserRepository(repository: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindsHomeRepository(repository: HomeRepositoryImpl): HomeRepository

    @Binds
    abstract fun bindsLoginRepository(repository: LoginRepositoryImpl): LoginRepository

    @Binds
    abstract fun bindsPolicyDetailRepository(repository: PolicyRepositoryImpl): PolicyRepository

    @Binds
    abstract fun bindsCommentRepository(repository: CommentRepositoryImpl): CommentRepository

    @Binds
    abstract fun bindsCommunityRepository(repository: CommunityRepositoryImpl): CommunityRepository

    @Binds
    abstract fun bindsSpecPolicyRepository(repository: SpecPolicyRepositoryImpl): SpecPolicyRepository

    @Binds
    abstract fun bindsSearchRepository(repository: SearchRepositoryImpl): SearchRepository

    @Binds
    abstract fun bindsAnnounceRepository(repository: AnnounceRepositoryImpl): AnnounceRepository

    @Binds
    abstract fun bindsReportRepository(repository: ReportRepositoryImpl): ReportRepository

    @Binds
    abstract fun bindsSseRepository(repository: SseRepositoryImpl): SseRepository
}
