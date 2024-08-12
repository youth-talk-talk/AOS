package com.core.dataapi.repository

import com.youthtalk.model.PolicyDetail
import kotlinx.coroutines.flow.Flow

interface PolicyDetailRepository {
    fun getPolicyDetail(policyId: String): Flow<PolicyDetail>
}
