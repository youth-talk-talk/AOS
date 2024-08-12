package com.youthtalk.mapper

import com.youthtalk.dto.CommentResponse
import com.youthtalk.model.Comment

fun CommentResponse.toDate() = Comment(
    commentId = commentId,
    nickname = nickname,
    content = content,
    isLikeByMember = isLikeByMember,
)
