package com.youthtalk.mapper

import com.youthtalk.dto.PolicyDetailResponse
import com.youthtalk.dto.SearchPoliciesResponse
import com.youthtalk.model.Policy
import com.youthtalk.model.PolicyDetail
import com.youthtalk.model.PolicyResponse
import com.youthtalk.model.SearchPolicy

fun PolicyResponse.toData() = Policy(
    policyId = policyId,
    category = category,
    title = title,
    deadlineStatus = deadlineStatus,
    hostDep = hostDep,
    scrap = scrap,
)

fun PolicyDetailResponse.toData() = PolicyDetail(
    title = title,
    introduction = introduction,
    supportDetail = supportDetail,
    applyTerm = applyTerm,
    operationTerm = operationTerm,
    age = age,
    addrIncome = addrIncome,
    education = education,
    major = major,
    employment = employment,
    specialization = specialization,
    applLimit = applLimit,
    addition = addition,
    applStep = applStep,
    evaluation = evaluation,
    applUrl = applUrl,
    submitDoc = submitDoc,
    etc = etc,
    hostDep = hostDep,
    operatingOrg = operatingOrg,
    refUrl1 = refUrl1,
    refUrl2 = refUrl2,
    formattedApplUrl = formattedApplUrl,
    isScrap = isScrap,
)

fun SearchPoliciesResponse.toData() = SearchPolicy(
    title = title,
    policyId = policyId,
)
