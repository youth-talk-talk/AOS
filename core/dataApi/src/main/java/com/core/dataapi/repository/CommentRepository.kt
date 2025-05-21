package com.core.dataapi.repository

import com.youthtalk.model.comment.CommentInfo
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    fun getPolicyComment(policyId: Long): Flow<CommentInfo>
    fun getPostDetailComments(postId: Long): Flow<CommentInfo>
    fun postPostAddComment(postId: Long, message: String): Flow<Long>
    fun patchComment(commentId: Long, message: String): Flow<Long>
    fun postLikes(commentId: Long, isLike: Boolean): Flow<String>
}
