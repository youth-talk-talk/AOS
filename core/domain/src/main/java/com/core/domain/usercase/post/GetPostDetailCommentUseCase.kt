package com.core.domain.usercase.post

import com.core.dataapi.repository.CommunityRepository
import javax.inject.Inject

class GetPostDetailCommentUseCase @Inject constructor(
    private val communityRepository: CommunityRepository,
) {
    operator fun invoke(id: Long) = communityRepository.getPostDetailComments(id)
}
