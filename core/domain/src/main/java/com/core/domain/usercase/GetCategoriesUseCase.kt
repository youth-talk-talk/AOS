package com.core.domain.usercase

import com.core.dataapi.repository.UserRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke() = userRepository.getCategoryList()
}
