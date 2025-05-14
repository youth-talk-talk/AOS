package com.core.dataapi.repository

import androidx.paging.PagingData
import com.youthtalk.model.post.Post
import com.youthtalk.model.post.PostSubject
import com.youthtalk.model.post.PostType
import com.youthtalk.model.typeenum.Category
import kotlinx.coroutines.flow.Flow

interface CommunityRepository {
    fun getPopularPosts(category: Category, postSubject: PostSubject): Flow<List<Post>>
    fun getPosts(category: Category, postType: PostType, postSubject: PostSubject): Flow<Flow<PagingData<Post>>>
}
