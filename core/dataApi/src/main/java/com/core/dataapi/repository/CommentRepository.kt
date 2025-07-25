package com.core.dataapi.repository

import com.youthtalk.model.comment.CommentInfo
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    suspend fun getPolicyComment(policyId: Long): Result<CommentInfo>
    suspend fun getPostDetailComments(postId: Long): Result<CommentInfo>
    fun postPostAddComment(postId: Long, message: String): Flow<Long>
    fun patchComment(commentId: Long, message: String): Flow<Long>
    fun postLikes(commentId: Long, isLike: Boolean): Flow<String>
}
