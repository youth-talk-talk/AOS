package com.core.domain.usercase.post

import com.core.dataapi.repository.CommunityRepository
import com.youthtalk.model.post.PostSubject
import com.youthtalk.model.post.PostType
import com.youthtalk.model.typeenum.Category
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
) {
    operator fun invoke(category: Category, postType: PostType, postSubject: PostSubject) =
        communityRepository.getPosts(category, postType, postSubject)
}
