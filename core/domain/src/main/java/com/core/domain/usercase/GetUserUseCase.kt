package com.core.domain.usercase

import com.core.dataapi.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke() = userRepository.getUser()
}
