package com.core.domain.usercase

import com.core.dataapi.repository.CommunityRepository
import javax.inject.Inject

class PostCommentLikeUseCase @Inject constructor(
    private val communityRepository: CommunityRepository,
) {
    operator fun invoke(id: Long, isLike: Boolean) = communityRepository.postCommentLike(id, isLike)
}
