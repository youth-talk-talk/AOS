package com.youthtalk.repository

import com.core.dataapi.repository.ReportRepository
import com.youthtalk.data.ReportService
import com.youthtalk.utils.ErrorUtils.createResult
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val reportService: ReportService
) : ReportRepository {
    override suspend fun reportPost(postId: Long): Result<Unit> {
        return createResult {
            reportService.reportPosts(postId)
        }
    }

    override suspend fun reportComment(commentId: Long): Result<Unit> {
        return createResult {
            reportService.reportComments(commentId)
        }
    }
}
