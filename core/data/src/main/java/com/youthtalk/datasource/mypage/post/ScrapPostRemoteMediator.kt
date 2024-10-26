package com.youthtalk.datasource.mypage.post

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.youthtalk.data.CommunityService
import com.youthtalk.datasource.room.YouthDatabase
import com.youthtalk.mapper.toData
import com.youthtalk.model.ScrapPost
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ScrapPostRemoteMediator @Inject constructor(
    private val communityService: CommunityService,
    private val youthDatabase: YouthDatabase,
    private val type: String,
) : RemoteMediator<Int, ScrapPost>() {
    private val scrapPostDao = youthDatabase.scrapPostDao()
    private val scrapPostRemoteKeyDao = youthDatabase.scrapPostRemoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        return super.initialize()
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, ScrapPost>): MediatorResult {
        val remoteKey = when (loadType) {
            LoadType.REFRESH -> {
                null
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(true)
            }
            LoadType.APPEND -> {
                scrapPostRemoteKeyDao.getNextKey()
            }
        }
        try {
            val page = remoteKey?.nextPage ?: 0
            val response = communityService.getMyPagePosts(
                page = page,
                size = state.config.pageSize,
                type = type,
            )
            val posts = response.data?.map { it.toData() } ?: listOf()
            youthDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    scrapPostDao.deleteAll()
                    scrapPostRemoteKeyDao.deleteAll()
                }
                scrapPostRemoteKeyDao.insertOrReplace(ScrapPostRemoteKey(nextPage = page + 1))
                scrapPostDao.insertAll(posts)
            }
            return MediatorResult.Success(posts.size != state.config.pageSize)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        }
    }
}
