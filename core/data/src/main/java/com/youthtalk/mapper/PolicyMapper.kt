package com.youthtalk.mapper

import com.youthtalk.model.Policy
import com.youthtalk.model.PolicyResponse

fun PolicyResponse.toData() = Policy(
    policyId = policyId,
    category = category,
    title = title,
    deadlineStatus = deadlineStatus,
    hostDep = hostDep,
    scrap = scrap,
)
