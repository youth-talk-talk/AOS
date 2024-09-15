package com.core.domain.usercase

import com.core.dataapi.repository.HomeRepository
import com.core.dataapi.repository.SpecPolicyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class PostPolicyScrapUseCase @Inject constructor(
    private val specPolicyRepository: SpecPolicyRepository,
    private val homeRepository: HomeRepository,
) {
    operator fun invoke(id: String, scrap: Boolean): Flow<Map<String, Boolean>> {
        return combine(
            specPolicyRepository.postScrap(id),
            homeRepository.postHomePolicyScrap(id, scrap),
        ) { _, map ->
            map
        }
    }
}
