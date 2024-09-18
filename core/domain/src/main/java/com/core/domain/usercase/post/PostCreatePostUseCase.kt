package com.core.domain.usercase.post

import com.core.dataapi.repository.CommunityRepository
import com.youthtalk.model.WriteInfo
import javax.inject.Inject

class PostCreatePostUseCase @Inject constructor(
    private val communityRepository: CommunityRepository,
) {
    operator fun invoke(postType: String, title: String, policyId: String?, contents: List<WriteInfo>) =
        communityRepository.postCreate(postType = postType, title = title, policyId = policyId, content = contents)
}
