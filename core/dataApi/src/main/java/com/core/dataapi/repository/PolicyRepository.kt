package com.core.dataapi.repository

import com.youthtalk.model.policy.Policy
import com.youthtalk.model.policy.PolicyDetail

interface PolicyRepository {
    suspend fun getPolicyDetail(policyId: Long): Result<PolicyDetail>
    suspend fun getRecentlyViewPolicies(): Result<List<Policy>>
}
