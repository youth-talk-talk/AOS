package com.core.domain.usercase.policydetail

import com.core.dataapi.repository.CommentRepository
import com.youthtalk.model.CommentInfo
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetPolicyDetailCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) {
    operator fun invoke(policyId: Long): Flow<CommentInfo> = commentRepository.getPolicyComment(policyId)
}
