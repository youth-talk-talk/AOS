package com.core.domain.usercase

import com.core.dataapi.repository.SpecPolicyRepository
import javax.inject.Inject

class PostPolicyScrapUseCase @Inject constructor(
    private val specPolicyRepository: SpecPolicyRepository
) {
    suspend operator fun invoke(id: Long, scrap: Boolean) = specPolicyRepository.postScrap(id, scrap)
}
