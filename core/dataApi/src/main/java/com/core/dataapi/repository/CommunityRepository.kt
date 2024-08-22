package com.core.dataapi.repository

import androidx.paging.PagingData
import com.youthtalk.model.Post
import kotlinx.coroutines.flow.Flow

interface CommunityRepository {
    fun postReviewPost(): Flow<Flow<PagingData<Post>>>
    fun postPopularReviewPost(): Flow<List<Post>>
    fun getPopularPosts(): Flow<List<Post>>
    fun getPosts(): Flow<Flow<PagingData<Post>>>
}
