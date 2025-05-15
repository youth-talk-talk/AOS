package com.youthtalk.datasource.post

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.youthtalk.data.CommunityService
import com.youthtalk.datasource.room.YouthDatabase
import com.youthtalk.mapper.toDomain
import com.youthtalk.model.post.Post
import com.youthtalk.model.post.PostSubject
import com.youthtalk.model.post.PostType
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.delay
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class PostKeywordRemoteMediator @Inject constructor(
    private val communityService: CommunityService,
    private val youthDatabase: YouthDatabase,
    private val keyword: String,
    private val postType: PostType,
    private val postSubject: PostSubject
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
            youthDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    postDao.deleteAll(postType)
                    postRemoteKeyDao.deleteAll()
                }
                delay(300)
            }

            val page = remoteKey?.nextPage ?: 0
            val type = when (postSubject) {
                PostSubject.REVIEW -> "review"
                PostSubject.FREE -> "post"
            }

            val response = communityService.getSearchPosts(
                keyword = keyword,
                page = page,
                type = type,
                size = state.config.pageSize
            )
            val posts = response.data?.posts?.map { it.toDomain().copy(postType = postType) } ?: listOf()
            youthDatabase.withTransaction {
                postRemoteKeyDao.insertOrReplace(PostRemoteKey(nextPage = page + 1, postType = postType))
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
