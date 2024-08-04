package com.youthtalk.model

import kotlinx.serialization.Serializable

@Serializable
data class PolicyResponse(
    val policyId: String,
    val category: Category,
    val title: String,
    val deadlineStatus: String,
    val hostDep: String,
    val scrap: Boolean,
)
