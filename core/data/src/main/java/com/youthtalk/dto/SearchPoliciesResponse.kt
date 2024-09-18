package com.youthtalk.dto

import kotlinx.serialization.Serializable

@Serializable
data class SearchPoliciesResponse(
    val title: String,
    val policyId: String,
)
