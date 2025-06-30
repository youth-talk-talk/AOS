package com.core.dataapi.repository

interface ReportRepository {
    suspend fun reportPost(postId: Long): Result<Unit>
    suspend fun reportComment(commentId: Long): Result<Unit>
}
