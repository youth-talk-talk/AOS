package com.core.dataapi.repository

import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun getRecentList(): Flow<List<String>>
    fun getPostsCount(type: String, keyword: String): Flow<Int>
    suspend fun postRecentList(recentList: List<String>)
}
