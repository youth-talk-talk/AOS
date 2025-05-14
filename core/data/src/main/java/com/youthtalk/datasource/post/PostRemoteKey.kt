package com.youthtalk.datasource.post

import androidx.room.Entity
import com.youthtalk.model.post.PostType

@Entity(primaryKeys = ["nextPage", "postType"])
data class PostRemoteKey(
    val nextPage: Int,
    val postType: PostType
)
