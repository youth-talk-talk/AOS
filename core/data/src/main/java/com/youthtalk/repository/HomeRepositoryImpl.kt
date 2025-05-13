package com.youthtalk.repository

import com.core.dataapi.repository.HomeRepository
import com.core.datastore.datasource.DataStoreDataSource
import com.core.exception.NoDataException
import com.youthtalk.data.PolicyService
import com.youthtalk.mapper.toDomain
import com.youthtalk.model.home.HomeData
import com.youthtalk.model.home.NewPolicies
import com.youthtalk.model.typeenum.SortType
import com.youthtalk.utils.ErrorUtils.throwableError
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class HomeRepositoryImpl @Inject constructor(
    private val policyService: PolicyService,
    private val dataSource: DataStoreDataSource
) : HomeRepository {
    override fun getHome(): Flow<HomeData> = flow {
        Timber.e("HomeRepositoryImpl getHome start")
        runCatching { policyService.getHome() }
            .onSuccess { homeData ->
                Timber.e("HomeRepositoryImpl getHome Success $homeData")
                homeData.data?.let { data ->
                    emit(data.toDomain())
                } ?: throw NoDataException()
            }
            .onFailure { error ->
                Timber.e("HomeRepositoryImpl getHome  error : $error")
                throwableError<HomeData>(error)
            }
    }

    override fun getNewPolicies(sortType: SortType): Flow<NewPolicies> = flow {
        Timber.e("HomeRepositoryImpl getHome start")
        runCatching { policyService.getNewPolicies(sortType) }
            .onSuccess { homeData ->
                Timber.e("HomeRepositoryImpl getNewPolicies Success $homeData")
                homeData.data?.let { data ->
                    emit(data.toDomain())
                } ?: throw NoDataException()
            }
            .onFailure { error ->
                Timber.e("HomeRepositoryImpl getNewPolicies  error : $error")
                throwableError<HomeData>(error)
            }
    }
}
