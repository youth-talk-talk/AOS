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
import java.io.File
import kotlinx.coroutines.flow.Flow

interface CommunityRepository {
    fun getPopularPosts(category: Category, postSubject: PostSubject): Flow<List<Post>>
    fun getPosts(category: Category, postType: PostType, postSubject: PostSubject): Flow<Flow<PagingData<Post>>>
    fun getListImage(): Flow<List<Image>>
    fun postUploadImage(file: File): Flow<String>
    fun postCreatePost(createPost: CreatePost): Flow<Long>
    fun getPostDetail(postId: Long): Flow<PostDetail>
    fun deletePost(postId: Long): Flow<Long>
    fun postPostScrap(postId: Long, scrap: Boolean): Flow<Long>
    fun syncPostScrap(reviews: List<Post>, frees: List<Post>): Flow<Pair<List<Post>, List<Post>>>
    fun getSettingPosts(isScrapType: Boolean): Flow<Flow<PagingData<Post>>>
    fun getSettingPostCount(isScrapType: Boolean): Flow<Int>
    fun postModifyPost(postId: Long, modifyPost: ModifyPost): Flow<Long>
}
