package com.core.domain.usercase.report

import com.core.dataapi.repository.ReportRepository
import javax.inject.Inject

class ReportCommentUseCase @Inject constructor(
    private val reportRepository: ReportRepository
) {

    suspend operator fun invoke(commentId: Long): Result<Unit> {
        return reportRepository.reportComment(commentId)
    }
}
