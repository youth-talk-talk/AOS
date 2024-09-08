package com.core.domain.usercase.mypage

import com.core.dataapi.repository.MyPageRepository
import javax.inject.Inject

class PostUserLogoutUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository,
) {
    operator fun invoke(deleteUser: Boolean) = myPageRepository.postLogout(deleteUser)
}
