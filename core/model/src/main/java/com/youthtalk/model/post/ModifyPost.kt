package com.youthtalk.model.post

data class ModifyPost(
    val title: String,
    val postType: String,
    val policyId: String?,
    val contentList: List<PostContent>,
    val addImgUrlList: List<String>,
    val deletedImgUrlList: List<String>
)
