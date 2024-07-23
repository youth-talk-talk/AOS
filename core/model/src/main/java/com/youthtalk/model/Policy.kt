package com.youthtalk.model

data class Policy(
    val policyId: String,
    val category: Category,
    val title: String,
    val deadlineStatus: String,
    val hostDep: String,
    val scrap: Boolean,
)
