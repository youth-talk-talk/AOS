package com.youthtalk.mapper

import com.youthtalk.dto.UserResponse
import com.youthtalk.model.User
import com.youthtalk.model.typeenum.Region

fun UserResponse.toData(): User = User(
    memberId = memberId,
    nickname = nickname,
    region = Region.entries.find { it.region == region } ?: Region.ALL
)
