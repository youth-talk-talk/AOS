package com.core.domain.usercase

import com.core.dataapi.repository.SpecPolicyRepository
import javax.inject.Inject

class PostDeleteCommentUseCase @Inject constructor(
    private val specPolicyRepository: SpecPolicyRepository,
) {
    operator fun invoke(commentId: Long) = specPolicyRepository.postDeleteComment(commentId)
}
