package com.youthtalk.mapper

import com.youthtalk.dto.UserResponse
import com.youthtalk.model.Region
import com.youthtalk.model.User

fun UserResponse.toData(): User = User(
    memberId = memberId,
    nickname = nickname,
    region = Region.entries.find { it.region == region } ?: Region.ALL,
)
