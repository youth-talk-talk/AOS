package com.core.dataapi.repository

import androidx.paging.PagingData
import com.youthtalk.model.Comment
import com.youthtalk.model.Policy
import com.youthtalk.model.Post
import kotlinx.coroutines.flow.Flow

interface MyPageRepository {
    fun getScrapPolicies(): Flow<Flow<PagingData<Policy>>>
    fun getMyPagePosts(type: String): Flow<Flow<PagingData<Post>>>
    fun getMyPageComments(isMine: Boolean): Flow<List<Comment>>
}
