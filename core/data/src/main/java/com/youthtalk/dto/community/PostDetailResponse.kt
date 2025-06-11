package com.youthtalk.dto.community

import com.youthtalk.model.typeenum.Category
import kotlinx.serialization.Serializable

@Serializable
data class PostDetailResponse(
    val postId: Long,
    val postType: String,
    val title: String,
    val contentList: List<PostContentInfoResponse>,
    val policyId: Long?,
    val policyTitle: String?,
    val writerId: Long?,
    val nickname: String?,
    val view: Long,
    val profileImage: String?,
    val category: Category?,
    val updatedAt: String,
    val scrap: Boolean
)

@Serializable
data class PostContentInfoResponse(
    val content: String,
    val type: String
)
