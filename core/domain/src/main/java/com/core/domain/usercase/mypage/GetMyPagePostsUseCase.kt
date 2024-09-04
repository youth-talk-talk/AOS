package com.core.domain.usercase.mypage

import com.core.dataapi.repository.MyPageRepository
import javax.inject.Inject

class GetMyPagePostsUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository,
) {
    operator fun invoke(type: String) = myPageRepository.getMyPagePosts(type)
}
