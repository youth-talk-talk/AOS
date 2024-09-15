package com.core.domain.usercase.post

import com.core.dataapi.repository.CommunityRepository
import javax.inject.Inject

class PostPostAddCommentUseCase @Inject constructor(
    private val communityRepository: CommunityRepository,
) {
    operator fun invoke(id: Long, text: String) = communityRepository.postAddComment(id, text)
}
