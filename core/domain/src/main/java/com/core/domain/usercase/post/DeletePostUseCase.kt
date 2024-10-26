package com.core.domain.usercase.post

import com.core.dataapi.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeletePostUseCase @Inject constructor(
    private val communityRepository: CommunityRepository,
) {
    operator fun invoke(postId: Long): Flow<String> = communityRepository.deletePost(postId)
}
