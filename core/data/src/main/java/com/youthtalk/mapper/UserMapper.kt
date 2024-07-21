package com.youthtalk.mapper

import com.youthtalk.model.Region
import com.youthtalk.model.User
import com.youthtalk.model.UserResponse

fun UserResponse.toData(): User = User(
    memberId = memberId,
    nickname = nickname,
    region = Region.entries.find { it.region == region } ?: Region.ALL,
)
