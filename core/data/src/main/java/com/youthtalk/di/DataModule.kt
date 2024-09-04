package com.youthtalk.di

import com.core.dataapi.repository.CommentRepository
import com.core.dataapi.repository.CommunityRepository
import com.core.dataapi.repository.HomeRepository
import com.core.dataapi.repository.LoginRepository
import com.core.dataapi.repository.MyPageRepository
import com.core.dataapi.repository.PolicyDetailRepository
import com.core.dataapi.repository.SpecPolicyRepository
import com.core.dataapi.repository.UserRepository
import com.core.datastore.datasource.DataSource
import com.core.datastore.datasource.DataStoreDataSource
import com.youthtalk.repository.CommentRepositoryImpl
import com.youthtalk.repository.CommunityRepositoryImpl
import com.youthtalk.repository.HomeRepositoryImpl
import com.youthtalk.repository.LoginRepositoryImpl
import com.youthtalk.repository.MyPageRepositoryImpl
import com.youthtalk.repository.PolicyDetailRepositoryImpl
import com.youthtalk.repository.SpecPolicyRepositoryImpl
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
    abstract fun bindsPolicyDetailRepository(repository: PolicyDetailRepositoryImpl): PolicyDetailRepository

    @Binds
    abstract fun bindsCommentRepository(repository: CommentRepositoryImpl): CommentRepository

    @Binds
    abstract fun bindsCommunityRepository(repository: CommunityRepositoryImpl): CommunityRepository

    @Binds
    abstract fun bindsSpecPolicyRepository(repository: SpecPolicyRepositoryImpl): SpecPolicyRepository

    @Binds
    abstract fun bindsMyPageRepository(repository: MyPageRepositoryImpl): MyPageRepository
}
