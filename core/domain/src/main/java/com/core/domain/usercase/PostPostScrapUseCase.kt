package com.core.domain.usercase

import com.core.dataapi.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class PostPostScrapUseCase @Inject constructor(
    private val communityRepository: CommunityRepository,
) {
    operator fun invoke(postId: Long, scrap: Boolean): Flow<Map<Long, Boolean>> {
        return combine(
            communityRepository.postPostScrap(postId),
            communityRepository.postPostScrapMap(postId, scrap),
        ) { _, map ->
            map
        }
    }
}
