package com.youthtalk.mapper

import com.youthtalk.dto.PolicyDetailResponse
import com.youthtalk.model.PolicyDetail

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
    isScrap = isScrap
)
