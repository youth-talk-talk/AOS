package com.youthtalk.repository

import com.core.dataapi.repository.HomeRepository
import com.core.exception.NoDataException
import com.youthtalk.data.PolicyService
import com.youthtalk.mapper.toDomain
import com.youthtalk.model.home.HomeData
import com.youthtalk.model.home.NewPolicies
import com.youthtalk.model.typeenum.SortType
import com.youthtalk.utils.ErrorUtils.createResult
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val policyService: PolicyService
) : HomeRepository {
    override suspend fun getHome(): Result<HomeData> = createResult {
        policyService.getHome().data?.toDomain() ?: throw NoDataException()
    }

    override suspend fun getNewPolicies(sortType: SortType): Result<NewPolicies> = createResult {
        policyService.getNewPolicies(sortType).data?.toDomain() ?: throw NoDataException()
    }
}
