package com.youthtalk.datasource.post

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.youthtalk.data.CommunityService
import com.youthtalk.mapper.toData
import com.youthtalk.model.Post
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PostPagingSource @Inject constructor(
    private val communityService: CommunityService,
) : PagingSource<Int, Post>() {
    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        try {
            val pageNumber = params.key ?: 0

            val response = communityService.getPosts(
                page = pageNumber,
                size = params.loadSize,
            )

            val posts = response.data?.posts?.map { it.toData() } ?: listOf()
            return LoadResult.Page(
                data = posts,
                prevKey = null,
                nextKey = if (posts.size == params.loadSize) pageNumber + 1 else null,
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }
}
