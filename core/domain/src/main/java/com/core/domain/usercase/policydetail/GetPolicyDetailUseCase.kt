package com.core.domain.usercase.policydetail

import com.core.dataapi.repository.PolicyDetailRepository
import com.youthtalk.model.PolicyDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPolicyDetailUseCase @Inject constructor(
    private val policyDetailRepository: PolicyDetailRepository,
) {
    operator fun invoke(policyId: String): Flow<PolicyDetail> = policyDetailRepository.getPolicyDetail(policyId)
}
