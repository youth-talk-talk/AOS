package com.core.domain.usercase.home

import com.core.dataapi.repository.HomeRepository
import com.youthtalk.model.enum.SortType
import javax.inject.Inject

class GetNewPolicesUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(sortType: SortType = SortType.RECENT) = homeRepository.getNewPolicies(sortType)
}
