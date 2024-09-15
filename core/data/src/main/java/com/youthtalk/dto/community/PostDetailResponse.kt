package com.youthtalk.dto.community

import kotlinx.serialization.Serializable

@Serializable
data class PostDetailResponse(
    val postId: Long,
    val postType: String,
    val title: String,
    val content: String,
    val contentList: List<PostContentInfoResponse>,
    val policyId: String?,
    val policyTitle: String?,
    val writerId: Long,
    val nickname: String?,
    val view: Long,
    val images: List<String>,
    val category: String?,
)

@Serializable
data class PostContentInfoResponse(
    val content: String,
    val type: String,
)
