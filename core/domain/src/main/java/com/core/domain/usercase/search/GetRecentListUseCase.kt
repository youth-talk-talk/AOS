package com.core.domain.usercase.search

import com.core.dataapi.repository.SearchRepository
import javax.inject.Inject

class GetRecentListUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
) {
    operator fun invoke() = searchRepository.getRecentList()
}
