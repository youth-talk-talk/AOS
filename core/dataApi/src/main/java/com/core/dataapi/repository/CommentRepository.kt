package com.core.dataapi.repository

import com.youthtalk.model.CommentInfo
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    fun getPolicyComment(policyId: String): Flow<CommentInfo>
    fun getPostDetailComments(postId: Long): Flow<CommentInfo>
    fun postPostAddComment(postId: Long, message: String): Flow<Long>
}
