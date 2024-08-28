package com.youthtalk.dto.specpolicy

import com.youthtalk.model.PolicyResponse
import kotlinx.serialization.Serializable

@Serializable
data class SpecPoliciesResponse(
    val totalCount: Int,
    val policyList: List<PolicyResponse>,
)
