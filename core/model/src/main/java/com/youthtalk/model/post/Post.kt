package com.youthtalk.model.post

import androidx.room.Entity

@Entity(primaryKeys = ["postId", "postType"])
data class Post(
    val postId: Long,
    val title: String,
    val writerId: Long?,
    val scraps: Int,
    val scrap: Boolean,
    val comments: Int,
    val policyId: String?,
    val policyTitle: String?,
    val contentPreview: String,
    val postType: PostType = PostType.COMMUNITY_TAB_REVIEW
)
