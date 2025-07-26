package com.core.domain.usercase.post

import com.core.dataapi.repository.CommunityRepository
import com.youthtalk.model.post.Post
import javax.inject.Inject

class SyncPopularPostUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(reviews: List<Post>, frees: List<Post>) = communityRepository.syncPostScrap(reviews, frees)
}
