package com.core.domain.usercase.search

import com.core.dataapi.repository.SearchRepository
import javax.inject.Inject

class GetSearchPostCountUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
) {
    operator fun invoke(type: String, keyword: String) = searchRepository.getPostsCount(type, keyword)
}
