package com.core.domain.usercase.home

import com.core.dataapi.repository.HomeRepository
import javax.inject.Inject

class GetHomeDataUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    operator fun invoke() = homeRepository.getHome()
}
