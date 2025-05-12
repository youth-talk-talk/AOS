package com.core.domain.usercase

import com.core.dataapi.repository.HomeRepository
import com.core.dataapi.repository.SpecPolicyRepository
import javax.inject.Inject
import timber.log.Timber

class PostPolicyScrapUseCase @Inject constructor(
    private val specPolicyRepository: SpecPolicyRepository,
    private val homeRepository: HomeRepository
) {
    operator fun invoke(id: String, scrap: Boolean) {
        Timber.e("비어있음")
    }
}
