package com.core.domain.usercase.policy

import com.core.dataapi.repository.SpecPolicyRepository
import javax.inject.Inject

class GetScrapPolicyUseCase @Inject constructor(
    private val policyRepository: SpecPolicyRepository
) {
    operator fun invoke() = policyRepository.getScrapPolicies()
}
