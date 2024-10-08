package com.core.dataapi.repository

import androidx.paging.PagingData
import com.youthtalk.model.Comment
import com.youthtalk.model.Policy
import com.youthtalk.model.Post
import com.youthtalk.model.Region
import com.youthtalk.model.User
import kotlinx.coroutines.flow.Flow

interface MyPageRepository {
    fun getScrapPolicies(): Flow<Flow<PagingData<Policy>>>
    fun getMyPagePosts(type: String): Flow<PagingData<Post>>
    fun getMyPageComments(isMine: Boolean): Flow<List<Comment>>
    fun getDeadlinePolicies(): Flow<List<Policy>>
    fun postUser(nickname: String, region: Region): Flow<User>
    fun postLogout(deleteUser: Boolean): Flow<Boolean>
}
