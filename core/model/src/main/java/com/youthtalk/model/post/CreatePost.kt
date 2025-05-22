package com.youthtalk.model.post

data class CreatePost(
    val title: String,
    val postType: String,
    val policyId: String?,
    val contentList: List<PostContent>
)

data class PostContent(
    val content: String,
    val type: String
)
