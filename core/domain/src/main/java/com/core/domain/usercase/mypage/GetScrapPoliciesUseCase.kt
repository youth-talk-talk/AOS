package com.core.domain.usercase.mypage

import com.core.dataapi.repository.MyPageRepository
import javax.inject.Inject

class GetScrapPoliciesUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository,
) {
    operator fun invoke() = myPageRepository.getScrapPolicies()
}
