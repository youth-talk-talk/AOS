package com.youthtalk.model

import com.youthtalk.model.typeenum.Category
import java.time.LocalDateTime

data class PostDetail(
    val postId: Long,
    val postType: String,
    val title: String,
    val contentList: List<PostContentInfo>,
    val policyId: Long?,
    val policyTitle: String?,
    val writerId: Long,
    val nickname: String?,
    val view: Long,
    val profileImage: String?,
    val category: Category?,
    val updatedAt: LocalDateTime,
    val scrap: Boolean
)

data class PostContentInfo(
    val content: String,
    val type: String
)
