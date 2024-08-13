package com.youthtalk.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.core.dataapi.repository.CommunityRepository
import com.core.datastore.datasource.DataStoreDataSource
import com.core.exception.NoDataException
import com.youthtalk.data.CommunityService
import com.youthtalk.datasource.review.ReviewPostPagingSource
import com.youthtalk.dto.ReviewPostRequest
import com.youthtalk.mapper.toData
import com.youthtalk.model.ReviewPost
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CommunityRepositoryImpl @Inject constructor(
    private val communityService: CommunityService,
    private val dataStoreDataSource: DataStoreDataSource,
) : CommunityRepository {
    override fun postReviewPost(): Flow<Flow<PagingData<ReviewPost>>> = flow {
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

    override fun postPopularReviewPost(): Flow<List<ReviewPost>> = flow {
        val categories = dataStoreDataSource.getReviewCategoryFilter().first()
        runCatching {
            communityService.postReviewPosts(
                requestBody = ReviewPostRequest(categories).toRequestBody(),
                size = 0,
                page = 0,
            )
        }
            .onSuccess { response ->
                response.data?.let { popularReviewPosts ->
                    emit(popularReviewPosts.popularReviewPosts.map { it.toData() })
                } ?: throw NoDataException("no Data")
            }
            .onFailure {
                Log.d("YOON-CHAN", "CommnunityRepository postPopularReviewPost error ${it.message}")
            }
    }
}
