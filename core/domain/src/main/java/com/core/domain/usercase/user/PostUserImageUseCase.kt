package com.core.domain.usercase.user

import com.core.dataapi.repository.UserRepository
import java.io.File
import javax.inject.Inject

class PostUserImageUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(file: File?) = userRepository.postUserImage(file)
}
