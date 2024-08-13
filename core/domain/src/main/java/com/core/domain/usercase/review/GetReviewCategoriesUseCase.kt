package com.core.domain.usercase.review

import com.core.dataapi.repository.UserRepository
import javax.inject.Inject

class GetReviewCategoriesUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke() = userRepository.getReviewCategoryList()
}
