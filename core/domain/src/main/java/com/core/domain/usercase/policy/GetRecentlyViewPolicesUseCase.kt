package com.core.domain.usercase.policy

import com.core.dataapi.repository.PolicyRepository
import javax.inject.Inject

class GetRecentlyViewPolicesUseCase @Inject constructor(
    private val policyRepository: PolicyRepository
) {
    operator fun invoke() = policyRepository.getRecentlyViewPolicies()
}
