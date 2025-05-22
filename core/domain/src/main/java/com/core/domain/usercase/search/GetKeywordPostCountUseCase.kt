package com.core.domain.usercase.search

import com.core.dataapi.repository.SearchRepository
import com.youthtalk.model.post.PostSubject
import javax.inject.Inject

class GetKeywordPostCountUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    operator fun invoke(keyword: String, communityType: PostSubject) = searchRepository.getKeywordPostCount(keyword, communityType)
}
