package com.core.domain.usercase.specpolicy

import com.core.dataapi.repository.SpecPolicyRepository
import com.youthtalk.model.policy.PolicyType
import com.youthtalk.model.search.SearchFilter
import javax.inject.Inject

class PostSpecPoliciesUseCase @Inject constructor(
    private val specPolicyRepository: SpecPolicyRepository
) {
    operator fun invoke(searchFilter: SearchFilter, policyType: PolicyType) = specPolicyRepository.getPolicies(searchFilter, policyType)
}
