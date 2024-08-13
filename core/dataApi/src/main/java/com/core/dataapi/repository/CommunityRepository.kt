package com.core.dataapi.repository

import androidx.paging.PagingData
import com.youthtalk.model.ReviewPost
import kotlinx.coroutines.flow.Flow

interface CommunityRepository {
    fun postReviewPost(): Flow<Flow<PagingData<ReviewPost>>>
    fun postPopularReviewPost(): Flow<List<ReviewPost>>
}
