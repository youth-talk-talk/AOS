package com.core.domain.usercase.policydetail

import com.core.dataapi.repository.CommentRepository
import com.youthtalk.model.Comment
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPolicyDetailCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
) {
    operator fun invoke(policyId: String): Flow<List<Comment>> = commentRepository.getPolicyComment(policyId)
}
