package com.youthtalk.dto.post

import com.youthtalk.model.typeenum.Category
import kotlinx.serialization.Serializable

@Serializable
data class PostResponse(
    val postId: Long,
    val title: String,
    val writerId: Long?,
    val policyId: Long?,
    val policyTitle: String?,
    val comments: Int,
    val contentPreview: String,
    val scrapCount: Int,
    val scrap: Boolean,
    val category: Category? = null,
    val createdAt: String
)
