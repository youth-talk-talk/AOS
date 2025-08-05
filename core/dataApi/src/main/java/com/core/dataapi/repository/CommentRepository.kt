package com.core.dataapi.repository

import com.youthtalk.model.comment.CommentInfo

interface CommentRepository {
    suspend fun getPolicyComment(policyId: Long): Result<CommentInfo>
    suspend fun getPostDetailComments(postId: Long): Result<CommentInfo>
    suspend fun postPostAddComment(postId: Long, message: String): Result<Long>
    suspend fun patchComment(commentId: Long, message: String): Result<Long>
    suspend fun postLikes(commentId: Long, isLike: Boolean): Result<String>
}
