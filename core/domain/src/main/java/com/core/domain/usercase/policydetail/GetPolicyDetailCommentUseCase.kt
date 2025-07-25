package com.core.domain.usercase.policydetail

import com.core.dataapi.repository.CommentRepository
import com.youthtalk.model.comment.CommentInfo
import javax.inject.Inject

class GetPolicyDetailCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(policyId: Long): Result<CommentInfo> = commentRepository.getPolicyComment(policyId)
}
