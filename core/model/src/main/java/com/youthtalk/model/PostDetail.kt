package com.youthtalk.model

data class PostDetail(
    val postId: Long,
    val postType: String,
    val title: String,
    val content: String,
    val contentList: List<PostContentInfo>,
    val policyId: String?,
    val policyTitle: String?,
    val writerId: Long,
    val nickname: String?,
    val view: Long,
    val images: List<String>,
    val category: String?,
)

data class PostContentInfo(
    val content: String,
    val type: String,
)
