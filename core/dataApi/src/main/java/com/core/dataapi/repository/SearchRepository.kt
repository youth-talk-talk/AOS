package com.core.dataapi.repository

import androidx.paging.PagingData
import com.youthtalk.model.post.Post
import com.youthtalk.model.post.PostSubject
import com.youthtalk.model.post.PostType
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun getRecentList(): Flow<List<String>>
    fun getKeywordPost(keyword: String, communityType: PostSubject, postType: PostType): Flow<Flow<PagingData<Post>>>
    fun getKeywordPostCount(keyword: String, communityType: PostSubject): Flow<Int>
    suspend fun postRecentList(recentList: List<String>)
}
