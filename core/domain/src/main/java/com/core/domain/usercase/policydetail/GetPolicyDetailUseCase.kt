package com.core.domain.usercase.policydetail

import com.core.dataapi.repository.PolicyRepository
import com.youthtalk.model.policy.PolicyDetail
import javax.inject.Inject

class GetPolicyDetailUseCase @Inject constructor(
    private val policyRepository: PolicyRepository
) {
    suspend operator fun invoke(policyId: Long): Result<PolicyDetail> = policyRepository.getPolicyDetail(policyId)
}
