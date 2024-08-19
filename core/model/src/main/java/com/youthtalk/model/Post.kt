package com.youthtalk.model

data class Post(
val postId: Long,
val title: String,
val content: String,
val writerId: Long?,
val scraps: Int,
val scrap: Boolean,
val comments: Int,
val policyId: String?,
val policyTitle: String?,
)
