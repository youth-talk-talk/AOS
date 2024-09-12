package com.core.domain.usercase.specpolicy

import com.core.dataapi.repository.SpecPolicyRepository
import com.youthtalk.model.FilterInfo
import javax.inject.Inject

class PostFilterUseCase @Inject constructor(
    private val specPolicyRepository: SpecPolicyRepository,
) {
    operator fun invoke(filterInfo: FilterInfo) = specPolicyRepository.saveFilterInfo(filterInfo)
}
