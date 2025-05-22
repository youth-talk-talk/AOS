package com.youthtalk.datasource.policy

import androidx.room.Entity
import com.youthtalk.model.policy.PolicyType

@Entity(primaryKeys = ["nextPage", "policyType"])
data class PolicyRemoteKey(
    val nextPage: Int,
    val policyType: PolicyType
)
