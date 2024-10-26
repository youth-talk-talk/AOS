package com.core.domain.usercase

import com.core.dataapi.repository.CommunityRepository
import com.youthtalk.model.PostType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostPostScrapUseCase @Inject constructor(
    private val communityRepository: CommunityRepository,
) {
    operator fun invoke(postId: Long, scrap: Boolean, type: PostType): Flow<String> {
        return communityRepository.postPostScrap(postId, scrap, type)
    }
}
