package com.core.domain.usercase

import com.core.dataapi.repository.UserRepository
import com.youthtalk.model.Category
import javax.inject.Inject

class SetCategoriesUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(categories: List<Category>) = userRepository.setCategoryList(categories)
}
