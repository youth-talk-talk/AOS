package com.core.domain.usercase

import com.core.dataapi.repository.DataStoreRepository
import javax.inject.Inject

class CheckTokenUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
) {
    operator fun invoke() = dataStoreRepository.hasToken()
}
