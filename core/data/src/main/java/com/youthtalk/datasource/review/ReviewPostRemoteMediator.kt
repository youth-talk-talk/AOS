package com.youthtalk.datasource.review

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.core.datastore.datasource.DataStoreDataSource
import com.youthtalk.data.CommunityService
import com.youthtalk.datasource.room.YouthDatabase
import com.youthtalk.mapper.toReviewData
import com.youthtalk.model.ReviewPost
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class ReviewPostRemoteMediator @Inject constructor(
    private val communityService: CommunityService,
    private val youthDatabase: YouthDatabase,
    private val dataSource: DataStoreDataSource
) : RemoteMediator<Int, ReviewPost>() {
    private val reviewPostDao = youthDatabase.reviewPostDao()
    private val reviewPostRemoteKeyDao = youthDatabase.reviewPostRemoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        return super.initialize()
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, ReviewPost>): MediatorResult {
        val remoteKey = when (loadType) {
            LoadType.REFRESH -> {
                null
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(true)
            }
            LoadType.APPEND -> {
                reviewPostRemoteKeyDao.getNextKey()
            }
        }
        val categories = dataSource.getReviewCategoryFilter().first().map { it.name }
        try {
            val page = remoteKey?.nextPage ?: 0
            val response = communityService.postReviewPosts(
                categories = categories,
                page = page,
                size = state.config.pageSize
            )
            val reviewPosts = response.data?.posts?.map { it.toReviewData() } ?: listOf()
            youthDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    reviewPostDao.deleteAll()
                    reviewPostRemoteKeyDao.deleteAll()
                }
                reviewPostRemoteKeyDao.insertOrReplace(ReviewPostRemoteKey(nextPage = page + 1))
                reviewPostDao.insertAll(reviewPosts)
            }
            return MediatorResult.Success(reviewPosts.size != state.config.pageSize)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        }
    }
}
