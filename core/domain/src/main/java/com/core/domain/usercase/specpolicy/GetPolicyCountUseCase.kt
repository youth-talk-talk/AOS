package com.core.domain.usercase.specpolicy

import com.core.dataapi.repository.SpecPolicyRepository
import com.youthtalk.model.Category
import javax.inject.Inject

class GetPolicyCountUseCase @Inject constructor(
    private val specPolicyRepository: SpecPolicyRepository,
) {
    operator fun invoke(categories: List<Category>) = specPolicyRepository.getCount(categories)
}
