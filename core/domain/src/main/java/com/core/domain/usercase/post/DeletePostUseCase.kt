package com.core.domain.usercase.post

import com.core.dataapi.repository.CommunityRepository
import javax.inject.Inject

class DeletePostUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
) {
    operator fun invoke(postId: Long) = communityRepository.deletePost(postId)
}
