package com.core.domain.usercase.review

import com.core.dataapi.repository.UserRepository
import com.youthtalk.model.Category
import javax.inject.Inject

class SetReviewCategoriesUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(categories: List<Category>) = userRepository.setReviewCategoryList(categories)
}
