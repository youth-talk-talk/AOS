package com.youthtalk.mapper

import com.youthtalk.dto.PolicyDetailResponse
import com.youthtalk.model.PolicyDetail
import com.youthtalk.model.typeenum.Category
import com.youthtalk.model.typeenum.Region

fun PolicyDetailResponse.toData() = PolicyDetail(
    title = title,
    introduction = introduction,
    supportDetail = supportDetail,
    applyTerm = applyTerm,
    age = age,
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
    refUrl1 = refUrl1,
    refUrl2 = refUrl2,
    isScrap = isScrap,
    departmentImgUrl = departmentImgUrl,
    recruitmentType = recruitmentType,
    region = Region.entries.find { it.region == region },
    subRegion = subRegion,
    category = Category.entries.find { it.categoryName == category },
    earnEtc = earnEtc,
    marriage = marriage
)
