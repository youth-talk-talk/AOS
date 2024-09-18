package com.core.domain.usercase.post

import com.core.dataapi.repository.CommunityRepository
import com.youthtalk.model.WriteInfo
import javax.inject.Inject

class PostModifyPostUseCase @Inject constructor(
    private val communityRepository: CommunityRepository,
) {
    operator fun invoke(postId: Long, postType: String, title: String, policyId: String?, contents: List<WriteInfo>) =
        communityRepository.postModifyPost(
            postId = postId,
            postType = postType,
            title = title,
            policyId = policyId,
            content = contents,
        )
}
