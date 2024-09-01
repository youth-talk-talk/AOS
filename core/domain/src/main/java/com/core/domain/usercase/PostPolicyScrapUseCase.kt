package com.core.domain.usercase

import com.core.dataapi.repository.SpecPolicyRepository
import javax.inject.Inject

class PostPolicyScrapUseCase @Inject constructor(
    private val specPolicyRepository: SpecPolicyRepository,
) {
    operator fun invoke(id: String) = specPolicyRepository.postScrap(id)
}
