package com.core.domain.usercase.home

import com.core.dataapi.repository.HomeRepository
import com.youthtalk.model.typeenum.SortType
import javax.inject.Inject

class GetNewPolicesUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(sortType: SortType = SortType.RECENT) = homeRepository.getNewPolicies(sortType)
}
