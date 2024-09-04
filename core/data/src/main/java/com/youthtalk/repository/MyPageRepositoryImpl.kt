package com.youthtalk.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.core.dataapi.repository.MyPageRepository
import com.youthtalk.data.CommunityService
import com.youthtalk.data.PolicyService
import com.youthtalk.data.UserService
import com.youthtalk.datasource.PagingSize.MY_PAGE_POSTS_SIZE
import com.youthtalk.datasource.PagingSize.SCRAP_PAGE_SIZE
import com.youthtalk.datasource.mypage.MyPagePostsPagingSource
import com.youthtalk.datasource.mypage.ScrapPolicyPagingSource
import com.youthtalk.mapper.toDate
import com.youthtalk.model.Comment
import com.youthtalk.model.Policy
import com.youthtalk.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val policyService: PolicyService,
    private val communityService: CommunityService,
    private val userService: UserService,
) : MyPageRepository {
    override fun getScrapPolicies(): Flow<Flow<PagingData<Policy>>> = flow {
        emit(
            Pager(
                pagingSourceFactory = { ScrapPolicyPagingSource(policyService) },
                config = PagingConfig(
                    pageSize = SCRAP_PAGE_SIZE,
                ),
            ).flow,
        )
    }

    override fun getMyPagePosts(type: String): Flow<Flow<PagingData<Post>>> = flow {
        emit(
            Pager(
                pagingSourceFactory = {
                    MyPagePostsPagingSource(
                        communityService = communityService,
                        type = type,
                    )
                },
                config = PagingConfig(
                    pageSize = MY_PAGE_POSTS_SIZE,
                ),
            ).flow,
        )
    }

    override fun getMyPageComments(isMine: Boolean): Flow<List<Comment>> = flow {
        runCatching {
            if (isMine) userService.getMyComments() else userService.getLikeComments()
        }
            .onSuccess { response ->
                emit(response.data?.map { it.copy(isLikedByMember = !isMine).toDate() } ?: listOf())
            }
            .onFailure {
                Log.d("YOON-CHAN", "MyPageRepository getMyPageComments error ${it.message}")
            }
    }
}
