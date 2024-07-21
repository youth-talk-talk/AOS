package com.youthtalk.model

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val memberId: Long,
    val nickname: String,
    val region: String,
)
