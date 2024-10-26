package com.youthtalk.datasource.post

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.youthtalk.data.CommunityService
import com.youthtalk.datasource.room.YouthDatabase
import com.youthtalk.mapper.toData
import com.youthtalk.model.Post
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator @Inject constructor(
    private val communityService: CommunityService,
    private val youthDatabase: YouthDatabase,
) : RemoteMediator<Int, Post>() {
    private val postDao = youthDatabase.postDao()
    private val postRemoteKeyDao = youthDatabase.postRemoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        return super.initialize()
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Post>): MediatorResult {
        val remoteKey = when (loadType) {
            LoadType.REFRESH -> {
                null
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(true)
            }
            LoadType.APPEND -> {
                postRemoteKeyDao.getNextKey()
            }
        }
        try {
            val page = remoteKey?.nextPage ?: 0
            val response = communityService.getPosts(
                page = page,
                size = state.config.pageSize,
            )
            val posts = response.data?.posts?.map { it.toData() } ?: listOf()
            youthDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    postDao.deleteAll()
                    postRemoteKeyDao.deleteAll()
                }
                postRemoteKeyDao.insertOrReplace(PostRemoteKey(nextPage = page + 1))
                postDao.insertAll(posts)
            }
            return MediatorResult.Success(posts.size != state.config.pageSize)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        }
    }
}
