package com.core.domain.usercase.post

import com.core.dataapi.repository.CommunityRepository
import com.youthtalk.model.post.CreatePost
import javax.inject.Inject

class PostCreatePostUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
) {
    operator fun invoke(createPost: CreatePost) = communityRepository.postCreatePost(createPost)
}
