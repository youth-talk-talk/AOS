package com.youthtalk.repository

import com.core.dataapi.repository.PolicyRepository
import com.core.exception.NoDataException
import com.youthtalk.data.PolicyService
import com.youthtalk.mapper.toData
import com.youthtalk.mapper.toDomain
import com.youthtalk.model.policy.Policy
import com.youthtalk.model.policy.PolicyDetail
import com.youthtalk.utils.ErrorUtils.createResult
import javax.inject.Inject

class PolicyRepositoryImpl @Inject constructor(
    private val policyService: PolicyService
) : PolicyRepository {

    override suspend fun getPolicyDetail(policyId: Long): Result<PolicyDetail> = createResult {
        policyService.getPolicyDetail(policyId).data?.toData() ?: throw NoDataException()
    }

    override suspend fun getRecentlyViewPolicies(): Result<List<Policy>> = createResult {
        policyService.getRecentlyViewPolicies().data?.map { it.toDomain() } ?: throw NoDataException()
    }
}
