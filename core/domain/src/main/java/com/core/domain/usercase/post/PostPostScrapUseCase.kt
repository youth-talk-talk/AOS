package com.core.domain.usercase.post

import com.core.dataapi.repository.CommunityRepository
import javax.inject.Inject

class PostPostScrapUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
) {
    operator fun invoke(postId: Long, scrap: Boolean) = communityRepository.postPostScrap(postId, scrap)
}
