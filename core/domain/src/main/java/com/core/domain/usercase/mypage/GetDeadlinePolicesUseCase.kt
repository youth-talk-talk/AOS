package com.core.domain.usercase.mypage

import com.core.dataapi.repository.MyPageRepository
import javax.inject.Inject

class GetDeadlinePolicesUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository,
) {
    operator fun invoke() = myPageRepository.getDeadlinePolicies()
}
