package com.youthtalk.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val memberId: Long,
    val nickname: String,
    val region: String,
)
