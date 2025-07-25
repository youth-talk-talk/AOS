package com.youthtalk.repository

import com.core.dataapi.repository.HomeRepository
import com.core.exception.NoDataException
import com.youthtalk.data.PolicyService
import com.youthtalk.mapper.toDomain
import com.youthtalk.model.home.HomeData
import com.youthtalk.model.home.NewPolicies
import com.youthtalk.model.typeenum.SortType
import com.youthtalk.utils.ErrorUtils.createResult
import com.youthtalk.utils.ErrorUtils.throwableError
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class HomeRepositoryImpl @Inject constructor(
    private val policyService: PolicyService
) : HomeRepository {
    override suspend fun getHome(): Result<HomeData> = createResult {
        policyService.getHome().data?.toDomain() ?: throw NoDataException()
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
