package com.core.domain.usercase

import com.core.dataapi.repository.SpecPolicyRepository
import javax.inject.Inject

class PostPolicyAddCommentUseCase @Inject constructor(
    private val specPolicyRepository: SpecPolicyRepository
) {
    suspend operator fun invoke(policyId: Long, text: String) = specPolicyRepository.postAddComment(policyId, text)
}
