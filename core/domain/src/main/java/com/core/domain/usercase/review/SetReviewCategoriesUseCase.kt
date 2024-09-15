package com.core.domain.usercase.review

import androidx.paging.PagingData
import com.core.dataapi.repository.CommunityRepository
import com.core.dataapi.repository.UserRepository
import com.youthtalk.model.Category
import com.youthtalk.model.Post
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SetReviewCategoriesUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val communityRepository: CommunityRepository,
) {
    suspend operator fun invoke(categories: List<Category>): Flow<Flow<PagingData<Post>>> {
        userRepository.setReviewCategoryList(categories)
        return communityRepository.postReviewPost()
    }
}
