package com.core.dataapi.repository

import androidx.paging.PagingData
import com.youthtalk.model.FilterInfo
import com.youthtalk.model.Policy
import com.youthtalk.model.Post
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun getRecentList(): Flow<List<String>>
    fun getPolicies(filterInfo: FilterInfo, keyword: String): Flow<Flow<PagingData<Policy>>>
    fun getPoliciesCount(filterInfo: FilterInfo, keyword: String): Flow<Int>
    fun getPosts(type: String, keyword: String): Flow<Flow<PagingData<Post>>>
    fun getPostsCount(type: String, keyword: String): Flow<Int>
    suspend fun postRecentList(recentList: List<String>)
}
