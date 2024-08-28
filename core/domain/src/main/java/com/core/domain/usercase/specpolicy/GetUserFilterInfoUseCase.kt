package com.core.domain.usercase.specpolicy

import com.core.dataapi.repository.SpecPolicyRepository
import javax.inject.Inject

class GetUserFilterInfoUseCase @Inject constructor(
    private val specPolicyRepository: SpecPolicyRepository,
) {
    operator fun invoke() = specPolicyRepository.getFilterInfo()
}
