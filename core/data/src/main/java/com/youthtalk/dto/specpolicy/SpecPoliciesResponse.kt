package com.youthtalk.dto.specpolicy

import com.youthtalk.dto.policy.PolicyResponse
import kotlinx.serialization.Serializable

@Serializable
data class SpecPoliciesResponse(
    val totalCount: Int,
    val policyList: List<PolicyResponse>
)
