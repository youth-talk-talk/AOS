package com.core.domain.usercase.post

import com.core.dataapi.repository.CommunityRepository
import com.youthtalk.model.post.PostSubject
import com.youthtalk.model.typeenum.Category
import javax.inject.Inject

class GetPopularPostsUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
) {
    operator fun invoke(category: Category, postSubject: PostSubject) = communityRepository.getPopularPosts(category, postSubject)
}
