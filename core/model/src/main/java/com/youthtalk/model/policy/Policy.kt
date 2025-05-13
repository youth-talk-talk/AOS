package com.youthtalk.model.policy

import androidx.room.Entity
import com.youthtalk.model.Category
import kotlinx.serialization.Serializable

@Entity(primaryKeys = ["policyId", "policyType"])
@Serializable
data class Policy(
    val policyId: Long,
    val category: Category,
    val title: String,
    val deadlineStatus: String,
    val hostDep: String,
    val scrapCount: Int,
    val departmentImgUrl: String?,
    val region: String,
    val scrap: Boolean,
    val policyType: PolicyType = PolicyType.MAIN
)
