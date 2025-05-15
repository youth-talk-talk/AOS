package com.core.domain.usercase.search

import com.core.dataapi.repository.SearchRepository
import com.youthtalk.model.post.PostSubject
import com.youthtalk.model.post.PostType
import javax.inject.Inject

class GetKeywordPostUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    operator fun invoke(keyword: String, postSubject: PostSubject, postType: PostType) = searchRepository.getKeywordPost(
        keyword = keyword,
        communityType = postSubject,
        postType = postType
    )
}
