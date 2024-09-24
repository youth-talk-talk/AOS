package com.youthtalk.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.core.dataapi.repository.MyPageRepository
import com.core.datastore.datasource.DataStoreDataSource
import com.core.exception.NoDataException
import com.youthtalk.data.CommunityService
import com.youthtalk.data.PolicyService
import com.youthtalk.data.UserService
import com.youthtalk.datasource.PagingSize.MY_PAGE_POSTS_SIZE
import com.youthtalk.datasource.PagingSize.SCRAP_PAGE_SIZE
import com.youthtalk.datasource.mypage.MyPagePostsPagingSource
import com.youthtalk.datasource.mypage.ScrapPolicyPagingSource
import com.youthtalk.dto.CommentResponse
import com.youthtalk.dto.PostUserRequest
import com.youthtalk.dto.UserResponse
import com.youthtalk.mapper.toData
import com.youthtalk.mapper.toDate
import com.youthtalk.model.Comment
import com.youthtalk.model.Policy
import com.youthtalk.model.PolicyResponse
import com.youthtalk.model.Post
import com.youthtalk.model.Region
import com.youthtalk.model.User
import com.youthtalk.utils.ErrorUtils.throwableError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val policyService: PolicyService,
    private val communityService: CommunityService,
    private val userService: UserService,
    private val dataSource: DataStoreDataSource,
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

    override fun getMyPagePosts(type: String): Flow<PagingData<Post>> = Pager(
        pagingSourceFactory = {
            MyPagePostsPagingSource(
                communityService = communityService,
                type = type,
            )
        },
        config = PagingConfig(
            pageSize = MY_PAGE_POSTS_SIZE,
        ),
    ).flow

    override fun getMyPageComments(isMine: Boolean): Flow<List<Comment>> = flow {
        runCatching {
            if (isMine) userService.getMyComments() else userService.getLikeComments()
        }
            .onSuccess { response ->
                emit(response.data?.map { it.copy(isLikedByMember = !isMine).toDate() } ?: listOf())
            }
            .onFailure {
                throwableError<List<CommentResponse>>(it)
            }
    }

    override fun getDeadlinePolicies(): Flow<List<Policy>> = flow {
        runCatching {
            policyService.getDeadLinePolicies()
        }
            .onSuccess { response ->
                emit(response.data?.map { it.toData() } ?: listOf())
            }
            .onFailure {
                throwableError<List<PolicyResponse>>(it)
            }
    }

    override fun postUser(nickname: String, region: Region): Flow<User> = flow {
        val requestBody = PostUserRequest(nickname, region.region).toRequestBody()
        kotlin.runCatching {
            userService.postUser(requestBody)
        }
            .onSuccess { response ->
                response.data?.let {
                    emit(it.toData())
                } ?: throw NoDataException()
            }
            .onFailure {
                throwableError<UserResponse>(it)
            }
    }

    override fun postLogout(deleteUser: Boolean): Flow<Boolean> = flow {
        if (!deleteUser) {
            dataSource.clearData()
            emit(true)
        } else {
            runCatching {
                userService.postDeleteUser()
            }
                .onSuccess {
                    dataSource.clearData()
                    emit(true)
                }
                .onFailure {
                    throwableError<Int>(it)
                }
        }
    }
}
