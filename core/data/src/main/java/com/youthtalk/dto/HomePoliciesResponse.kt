package com.youthtalk.dto

import com.youthtalk.model.PolicyResponse
import kotlinx.serialization.Serializable

@Serializable
data class HomePoliciesResponse(
    val top5Policies: List<PolicyResponse>,
    val allPolicies: List<PolicyResponse>,
)
