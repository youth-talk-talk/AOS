package com.core.dataapi.repository

import androidx.paging.PagingData
import com.youthtalk.model.Comment
import com.youthtalk.model.Post
import com.youthtalk.model.PostDetail
import com.youthtalk.model.PostType
import com.youthtalk.model.ReviewPost
import com.youthtalk.model.WriteInfo
import java.io.File
import kotlinx.coroutines.flow.Flow

interface CommunityRepository {
    fun postReviewPost(): Flow<PagingData<ReviewPost>>
    fun postPopularReviewPost(): Flow<List<ReviewPost>>
    fun getPopularPosts(): Flow<List<Post>>
    fun getPosts(): Flow<PagingData<Post>>
    fun postPostScrap(id: Long, scrap: Boolean, type: PostType): Flow<String>
    fun patchComment(id: Long, content: String): Flow<String>
    fun postCommentLike(id: Long, like: Boolean): Flow<String>
    fun postAddComment(id: Long, text: String): Flow<Long>
    fun getPostDetail(id: Long): Flow<PostDetail>
    fun getPostDetailComments(id: Long): Flow<List<Comment>>
    fun uploadImage(file: File): Flow<String>
    fun postCreate(postType: String, title: String, content: List<WriteInfo>, policyId: String?): Flow<PostDetail>
    fun postModifyPost(postId: Long, postType: String, title: String, content: List<WriteInfo>, policyId: String?): Flow<PostDetail>
    fun postScrapPost(id: Long): Flow<Long>
    fun deletePost(postId: Long): Flow<String>
}
