package com.core.dataapi.repository

import androidx.paging.PagingData
import com.youthtalk.model.Comment
import com.youthtalk.model.Post
import com.youthtalk.model.PostDetail
import kotlinx.coroutines.flow.Flow

interface CommunityRepository {
    fun postReviewPost(): Flow<Flow<PagingData<Post>>>
    fun postPopularReviewPost(): Flow<List<Post>>
    fun getPopularPosts(): Flow<List<Post>>
    fun getPosts(): Flow<Flow<PagingData<Post>>>
    fun postPostScrap(id: Long): Flow<String>
    fun postPostScrapMap(id: Long, scrap: Boolean): Flow<Map<Long, Boolean>>
    fun patchComment(id: Long, content: String): Flow<String>
    fun postCommentLike(id: Long, like: Boolean): Flow<String>
    fun postAddComment(id: Long, text: String): Flow<Long>
    fun getPostDetail(id: Long): Flow<PostDetail>
    fun getPostDetailComments(id: Long): Flow<List<Comment>>
    fun getPostScrapMap(): Flow<Map<Long, Boolean>>
}
