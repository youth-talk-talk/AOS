package com.core.dataapi.repository

import androidx.paging.PagingData
import com.youthtalk.model.Image
import com.youthtalk.model.post.CreatePost
import com.youthtalk.model.post.ModifyPost
import com.youthtalk.model.post.Post
import com.youthtalk.model.post.PostDetail
import com.youthtalk.model.post.PostSubject
import com.youthtalk.model.post.PostType
import com.youthtalk.model.typeenum.Category
import kotlinx.coroutines.flow.Flow

interface CommunityRepository {
    suspend fun getPopularPosts(category: Category, postSubject: PostSubject): Result<List<Post>>
    fun getPosts(category: Category, postType: PostType, postSubject: PostSubject): Flow<Flow<PagingData<Post>>>
    suspend fun getListImage(): Result<List<Image>>
    suspend fun postCreatePost(createPost: CreatePost): Result<Long>
    suspend fun getPostDetail(postId: Long): Result<PostDetail>
    suspend fun deletePost(postId: Long): Result<Long>
    suspend fun postPostScrap(postId: Long, scrap: Boolean): Result<Long>
    fun syncPostScrap(reviews: List<Post>, frees: List<Post>): Flow<Pair<List<Post>, List<Post>>>
    fun getSettingPosts(isScrapType: Boolean): Flow<Flow<PagingData<Post>>>
    fun getSettingPostCount(isScrapType: Boolean): Flow<Int>
    fun postModifyPost(postId: Long, modifyPost: ModifyPost): Flow<Long>
}
