package com.youthtalk.dto

import kotlinx.serialization.Serializable

@Serializable
data class PolicyDetailResponse(
    val title: String,
    val introduction: String,
    val supportDetail: String,
    val applyTerm: String,
    val operationTerm: String,
    val age: String,
    val addrIncome: String,
    val education: String,
    val major: String,
    val employment: String,
    val specialization: String,
    val applLimit: String,
    val addition: String,
    val applStep: String,
    val evaluation: String,
    val applUrl: String,
    val submitDoc: String,
    val etc: String,
    val hostDep: String,
    val operatingOrg: String,
    val refUrl1: String,
    val refUrl2: String,
    val formattedApplUrl: String,
    val isScrap: Boolean,
)
