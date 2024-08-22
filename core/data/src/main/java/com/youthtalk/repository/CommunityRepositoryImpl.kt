package com.youthtalk.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.core.dataapi.repository.CommunityRepository
import com.core.datastore.datasource.DataStoreDataSource
import com.core.exception.NoDataException
import com.youthtalk.data.CommunityService
import com.youthtalk.datasource.post.PostPagingSource
import com.youthtalk.datasource.review.ReviewPostPagingSource
import com.youthtalk.mapper.toData
import com.youthtalk.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CommunityRepositoryImpl @Inject constructor(
    private val communityService: CommunityService,
    private val dataStoreDataSource: DataStoreDataSource,
) : CommunityRepository {
    override fun postReviewPost(): Flow<Flow<PagingData<Post>>> = flow {
        emit(
            Pager(
                config = PagingConfig(
                    pageSize = 10,
                ),
                pagingSourceFactory = {
                    ReviewPostPagingSource(
                        communityService = communityService,
                        dataSource = dataStoreDataSource,
                    )
                },
            ).flow,
        )
    }

    override fun postPopularReviewPost(): Flow<List<Post>> = flow {
        val categories = dataStoreDataSource.getReviewCategoryFilter().first().map { it.name }
        runCatching {
            communityService.postReviewPosts(
                categories = categories,
                size = 0,
                page = 0,
            )
        }
            .onSuccess { response ->
                response.data?.let { popularReviewPosts ->
                    emit(popularReviewPosts.popularPosts.map { it.toData() })
                } ?: throw NoDataException("no Data")
            }
            .onFailure {
                Log.d("YOON-CHAN", "CommnunityRepository postPopularReviewPost error ${it.message}")
            }
    }

    override fun getPopularPosts(): Flow<List<Post>> = flow {
        runCatching {
            communityService.getPosts(
                size = 0,
                page = 0,
            )
        }
            .onSuccess { response ->
                response.data?.let {
                    emit(it.popularPosts.map { it.toData() })
                } ?: throw NoDataException("no Data")
            }
            .onFailure {
                Log.d("YOON-CHAN", "CommnunityRepository postPopularReviewPost error ${it.message}")
            }
    }

    override fun getPosts(): Flow<Flow<PagingData<Post>>> = flow {
        emit(
            Pager(
                config = PagingConfig(
                    pageSize = 10,
                ),
                pagingSourceFactory = {
                    PostPagingSource(
                        communityService = communityService,
                    )
                },
            ).flow,
        )
    }
}
