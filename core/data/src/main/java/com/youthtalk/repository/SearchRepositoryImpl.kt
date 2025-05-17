package com.youthtalk.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.core.dataapi.repository.SearchRepository
import com.core.datastore.datasource.DataStoreDataSource
import com.core.exception.NoDataException
import com.youthtalk.data.CommunityService
import com.youthtalk.datasource.post.PostKeywordRemoteMediator
import com.youthtalk.datasource.room.YouthDatabase
import com.youthtalk.dto.PolicyDetailResponse
import com.youthtalk.model.post.Post
import com.youthtalk.model.post.PostSubject
import com.youthtalk.model.post.PostType
import com.youthtalk.utils.ErrorUtils.throwableError
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class SearchRepositoryImpl @Inject constructor(
    private val dataSource: DataStoreDataSource,
    private val communityService: CommunityService,
    private val youthDatabase: YouthDatabase
) : SearchRepository {
    override fun getRecentList(): Flow<List<String>> = dataSource.getRecentSearchList()

    override suspend fun postRecentList(recentList: List<String>) {
        dataSource.setRecentList(recentList)
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getKeywordPost(keyword: String, communityType: PostSubject, postType: PostType): Flow<Flow<PagingData<Post>>> = flow {
        emit(
            Pager(
                config = PagingConfig(
                    pageSize = 10,
                    enablePlaceholders = true
                ),
                remoteMediator = PostKeywordRemoteMediator(
                    communityService = communityService,
                    keyword = keyword,
                    postType = postType,
                    postSubject = communityType,
                    youthDatabase = youthDatabase
                )
            ) {
                youthDatabase.postDao().getPagingSource(postType = postType)
            }.flow
        )
    }

    override fun getKeywordPostCount(keyword: String, communityType: PostSubject): Flow<Int> = flow {
        val type = when (communityType) {
            PostSubject.REVIEW -> "review"
            PostSubject.POST -> "post"
        }
        runCatching {
            communityService.getSearchPosts(
                keyword = keyword,
                page = 0,
                type = type,
                size = 1
            )
        }
            .onSuccess { response ->
                response.data?.let { data ->
                    emit(data.total)
                } ?: throw NoDataException("no Data")
            }
            .onFailure {
                Timber.e("SearchRepositoryImpl getKeywordPostCount error $it")
                throwableError<PolicyDetailResponse>(it)
            }
    }
}
