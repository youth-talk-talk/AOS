package com.core.domain.usercase.search

import com.core.dataapi.repository.SearchRepository
import javax.inject.Inject

class GetSearchPoliciesTitleUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
) {
    operator fun invoke(title: String) = searchRepository.getSearchPolicies(title)
}
