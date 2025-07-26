package com.core.domain.usercase.comment

import com.core.dataapi.repository.UserRepository
import javax.inject.Inject

class GetSettingCommentUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(isLike: Boolean) = userRepository.getLikeComments(isLike)
}
