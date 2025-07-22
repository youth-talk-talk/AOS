package com.core.domain.usercase.user

import com.core.dataapi.repository.LoginRepository
import javax.inject.Inject

class PostLoginUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(socialId: String) = loginRepository.postLogin(socialId)
}
