package com.core.domain.usercase.review

import com.core.dataapi.repository.CommunityRepository
import javax.inject.Inject

class PostPopularReviewPostsUseCase @Inject constructor(
    private val communityRepository: CommunityRepository,
) {
    operator fun invoke() = communityRepository.postPopularReviewPost()
}
