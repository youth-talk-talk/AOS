package com.core.dataapi.repository

import com.youthtalk.model.PolicyDetail
import com.youthtalk.model.policy.Policy
import kotlinx.coroutines.flow.Flow

interface PolicyRepository {
    fun getPolicyDetail(policyId: String): Flow<PolicyDetail>
    fun getRecentlyViewPolicies(): Flow<List<Policy>>
}
