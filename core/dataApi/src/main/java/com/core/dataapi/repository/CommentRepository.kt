package com.core.dataapi.repository

import com.youthtalk.model.Comment
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    fun getPolicyComment(policyId: String): Flow<List<Comment>>
}
