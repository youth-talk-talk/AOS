package com.core.domain.usercase.user

import com.core.dataapi.repository.UserRepository
import javax.inject.Inject

class PostUserLogoutUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(deleteUser: Boolean) = userRepository.deleteUser(deleteUser)
}
