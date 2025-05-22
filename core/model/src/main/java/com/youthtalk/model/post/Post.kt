package com.youthtalk.model.post

import androidx.room.Entity
import com.youthtalk.model.typeenum.Category
import java.time.LocalDateTime

@Entity(primaryKeys = ["postId", "postType"])
data class Post(
    val postId: Long,
    val title: String,
    val writerId: Long,
    val policyId: Long?,
    val policyTitle: String?,
    val comments: Int,
    val contentPreview: String,
    val scrapCount: Int,
    val scrap: Boolean,
    val createdAt: LocalDateTime,
    val category: Category?,
    val postType: PostType = PostType.COMMUNITY_TAB_REVIEW
)
