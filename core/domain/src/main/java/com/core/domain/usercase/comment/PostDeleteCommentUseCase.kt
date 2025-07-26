package com.core.domain.usercase.comment

import com.core.dataapi.repository.SpecPolicyRepository
import javax.inject.Inject

class PostDeleteCommentUseCase @Inject constructor(
    private val specPolicyRepository: SpecPolicyRepository
) {
    suspend operator fun invoke(commentId: Long) = specPolicyRepository.postDeleteComment(commentId)
}
