package com.youthtalk.dto.policy

import com.youthtalk.model.typeenum.Category
import kotlinx.serialization.Serializable

@Serializable
data class PolicyResponse(
    val policyId: Long,
    val category: Category,
    val title: String,
    val deadlineStatus: String,
    val hostDep: String = "",
    val scrapCount: Int,
    val departmentImgUrl: String?,
    val region: String,
    val scrap: Boolean
)
