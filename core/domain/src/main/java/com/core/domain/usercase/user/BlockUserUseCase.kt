package com.core.domain.usercase.user

import com.core.dataapi.repository.UserRepository
import javax.inject.Inject

class BlockUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(userId: Long): Result<Unit> {
        return userRepository.blockUser(userId)
    }
}
