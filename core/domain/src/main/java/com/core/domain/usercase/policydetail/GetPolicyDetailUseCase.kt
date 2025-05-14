package com.core.domain.usercase.policydetail

import com.core.dataapi.repository.PolicyRepository
import com.youthtalk.model.PolicyDetail
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetPolicyDetailUseCase @Inject constructor(
    private val policyRepository: PolicyRepository
) {
    operator fun invoke(policyId: String): Flow<PolicyDetail> = policyRepository.getPolicyDetail(policyId)
}
