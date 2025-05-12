package com.youthtalk.model.policy

import com.youthtalk.model.Category

data class Policy(
    val policyId: Long,
    val category: Category,
    val title: String,
    val deadlineStatus: String,
    val hostDep: String,
    val scrapCount: Int,
    val departmentImgUrl: String?,
    val region: String,
    val scrap: Boolean
)
