package com.youthtalk.datasource.post

import android.util.Log
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
            Log.d("YOON-CHAN", "PostPagingSource getRefreshKey ${anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)}")
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
                prevKey = if (pageNumber == 0) null else pageNumber - 1,
                nextKey = if (posts.size != params.loadSize) null else pageNumber + 1,
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }
}
