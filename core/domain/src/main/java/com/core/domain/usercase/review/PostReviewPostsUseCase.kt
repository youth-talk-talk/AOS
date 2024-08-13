package com.core.domain.usercase.review

import com.core.dataapi.repository.CommunityRepository
import javax.inject.Inject

class PostReviewPostsUseCase @Inject constructor(
    private val communityRepository: CommunityRepository,
) {
    operator fun invoke() = communityRepository.postReviewPost()
}
