package com.core.domain.usercase.post

import com.core.dataapi.repository.CommunityRepository
import com.youthtalk.model.post.ModifyPost
import javax.inject.Inject

class PostModifyPostUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
) {
    operator fun invoke(postId: Long, modifyPost: ModifyPost) = communityRepository.postModifyPost(postId, modifyPost)
}
