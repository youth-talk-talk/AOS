package com.core.domain.usercase.mypage

import com.core.dataapi.repository.MyPageRepository
import javax.inject.Inject

class GetMyPageCommentsUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository,
) {
    operator fun invoke(isMine: Boolean) = myPageRepository.getMyPageComments(isMine)
}
