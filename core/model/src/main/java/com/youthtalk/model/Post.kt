package com.youthtalk.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Post(
    @PrimaryKey val postId: Long,
    val title: String,
    val writerId: Long?,
    val scraps: Int,
    val scrap: Boolean,
    val comments: Int,
    val policyId: String?,
    val policyTitle: String?
)
