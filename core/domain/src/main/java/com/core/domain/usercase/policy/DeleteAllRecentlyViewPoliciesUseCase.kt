package com.core.domain.usercase.policy

import com.core.dataapi.repository.SpecPolicyRepository
import javax.inject.Inject

class DeleteAllRecentlyViewPoliciesUseCase @Inject constructor(
    private val specPolicyRepository: SpecPolicyRepository
) {
    operator fun invoke() = specPolicyRepository.deleteAllRecentlyViewPolicies()
}
