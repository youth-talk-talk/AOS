package com.core.domain.usercase.report

import com.core.dataapi.repository.ReportRepository
import javax.inject.Inject

class ReportPostUseCase @Inject constructor(
    private val reportRepository: ReportRepository
) {

    suspend operator fun invoke(postId: Long): Result<Unit> {
        return reportRepository.reportPost(postId)
    }
}
