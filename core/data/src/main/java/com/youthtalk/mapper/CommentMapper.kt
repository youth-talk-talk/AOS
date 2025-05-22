package com.youthtalk.mapper

import com.youthtalk.dto.comment.CommentInfoResponse
import com.youthtalk.dto.comment.CommentResponse
import com.youthtalk.dto.comment.SettingCommentInfoResponse
import com.youthtalk.dto.comment.SettingCommentResponse
import com.youthtalk.model.comment.Comment
import com.youthtalk.model.comment.CommentInfo
import com.youthtalk.model.comment.SettingComment
import com.youthtalk.model.comment.SettingCommentInfo
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

fun SettingCommentInfoResponse.toData() = SettingCommentInfo(
    commentCount = commentCount,
    comments = comments.map { it.toData() }
)

fun SettingCommentResponse.toData() = SettingComment(
    commentId = commentId,
    writerId = writerId,
    nickname = nickname,
    content = content,
    articleId = articleId,
    articleType = articleType,
    articleTitle = articleTitle,
    isLikedByMember = isLikedByMember,
    likeCount = likeCount
)
