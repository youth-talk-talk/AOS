package com.core.domain.usercase

import com.core.dataapi.repository.SpecPolicyRepository
import javax.inject.Inject

class PostPolicyCommentUseCase @Inject constructor(
    private val specPolicyRepository: SpecPolicyRepository,
) {
    operator fun invoke(policyId: String, text: String) = specPolicyRepository.postAddComment(policyId, text)
}
