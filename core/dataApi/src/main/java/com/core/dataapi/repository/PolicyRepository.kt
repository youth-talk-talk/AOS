package com.core.dataapi.repository

import com.youthtalk.model.policy.Policy
import com.youthtalk.model.policy.PolicyDetail
import kotlinx.coroutines.flow.Flow

interface PolicyRepository {
    suspend fun getPolicyDetail(policyId: Long): Result<PolicyDetail>
    fun getRecentlyViewPolicies(): Flow<List<Policy>>
}
