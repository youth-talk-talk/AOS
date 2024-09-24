package com.core.domain.usercase

import com.core.dataapi.repository.LoginRepository
import javax.inject.Inject

class PostLoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
) {
    operator fun invoke(socialId: String) = loginRepository.postLogin(socialId)
}
