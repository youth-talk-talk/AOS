package com.core.domain.usercase

import com.core.dataapi.repository.LoginRepository
import javax.inject.Inject

class CheckTokenUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
) {
    operator fun invoke() = loginRepository.hasToken()
}
