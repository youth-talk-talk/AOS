package com.youthtalk.dto

import kotlinx.serialization.Serializable

@Serializable
data class PostAddCommentResponse(
    val commentId: Long,
)
