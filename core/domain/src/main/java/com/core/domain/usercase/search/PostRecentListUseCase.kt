package com.core.domain.usercase.search

import com.core.dataapi.repository.SearchRepository
import javax.inject.Inject

class PostRecentListUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
) {
    suspend operator fun invoke(recentList: List<String>) = searchRepository.postRecentList(recentList)
}
