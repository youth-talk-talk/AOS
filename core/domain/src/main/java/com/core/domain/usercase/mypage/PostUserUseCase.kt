package com.core.domain.usercase.mypage

import com.core.dataapi.repository.MyPageRepository
import com.youthtalk.model.Region
import javax.inject.Inject

class PostUserUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository,
) {
    operator fun invoke(nickname: String, region: Region) = myPageRepository.postUser(nickname, region)
}
