package com.core.domain.usercase.policy

import com.core.dataapi.repository.SpecPolicyRepository
import javax.inject.Inject

class PostSearchPolicyUseCase @Inject constructor(
    private val policyRepository: SpecPolicyRepository
) {
    operator fun invoke(searchPolicy: String) = policyRepository.searchPolicyName(searchPolicy)
}
