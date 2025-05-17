package com.youthtalk.mapper

import com.youthtalk.dto.comment.CommentInfoResponse
import com.youthtalk.dto.comment.CommentResponse
import com.youthtalk.model.Comment
import com.youthtalk.model.CommentInfo
import java.time.LocalDateTime

fun CommentInfoResponse.toData() = CommentInfo(
    commentCount = commentCount,
    comments = comments.map { it.toDate() }
)

fun CommentResponse.toDate() = Comment(
    commentId = commentId,
    writerId = writerId,
    nickname = nickname,
    content = content,
    isLikedByMember = isLikedByMember,
    profileImg = profileImg,
    createdAt = LocalDateTime.parse(createdAt.replace(" ", "T"))
)
