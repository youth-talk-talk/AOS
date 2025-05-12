package com.core.domain.usercase.review

import androidx.paging.PagingData
import com.core.dataapi.repository.CommunityRepository
import com.core.dataapi.repository.UserRepository
import com.youthtalk.model.Category
import com.youthtalk.model.ReviewPost
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class SetReviewCategoriesUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke(categories: List<Category>): Flow<PagingData<ReviewPost>> {
        userRepository.setReviewCategoryList(categories)
        return communityRepository.postReviewPost()
    }
}
