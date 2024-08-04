package com.core.domain.usercase

import com.core.dataapi.repository.HomeRepository
import com.core.dataapi.repository.UserRepository
import com.youthtalk.model.Category
import javax.inject.Inject
class ChangeCategoriesUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val homeRepository: HomeRepository,
) {
    suspend operator fun invoke(categories: List<Category>) {
        userRepository.setCategoryList(categories)
    }
}
