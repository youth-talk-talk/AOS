package com.youthtalk.model.policy

import kotlinx.serialization.Serializable

@Serializable
enum class PolicyType {
    MAIN,
    SEARCH,
    POLICY_TAB_DEADLINE,
    POLICY_TAB_CATEGORY,
    DEADLINE
}
