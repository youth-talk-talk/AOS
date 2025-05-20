package com.core.domain.usercase.user

import com.core.dataapi.repository.UserRepository
import com.youthtalk.model.typeenum.Region
import javax.inject.Inject

class PostUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(nickname: String, region: Region, imageUrl: String? = null) = userRepository.postUser(nickname, region, imageUrl)
}
