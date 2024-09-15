package com.youthtalk.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.core.dataapi.repository.CommunityRepository
import com.core.datastore.datasource.DataStoreDataSource
import com.core.exception.NoDataException
import com.youthtalk.data.CommentService
import com.youthtalk.data.CommunityService
import com.youthtalk.datasource.post.PostPagingSource
import com.youthtalk.datasource.review.ReviewPostPagingSource
import com.youthtalk.dto.CommentLikeRequest
import com.youthtalk.dto.community.PatchCommentRequest
import com.youthtalk.dto.community.PostAddCommentRequest
import com.youthtalk.mapper.toData
import com.youthtalk.mapper.toDate
import com.youthtalk.model.Comment
import com.youthtalk.model.Post
import com.youthtalk.model.PostDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CommunityRepositoryImpl @Inject constructor(
    private val communityService: CommunityService,
    private val commentService: CommentService,
    private val dataStoreDataSource: DataStoreDataSource,
) : CommunityRepository {
    companion object {
        var postScrapMap: Map<Long, Boolean> = mapOf()
    }

    override fun postReviewPost(): Flow<Flow<PagingData<Post>>> = flow {
        postScrapMap = mapOf()
        emit(
            Pager(
                config = PagingConfig(
                    pageSize = 10,
                    initialLoadSize = 10,
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
                    initialLoadSize = 10,
                ),
                pagingSourceFactory = {
                    PostPagingSource(
                        communityService = communityService,
                    )
                },
            ).flow,
        )
    }

    override fun postPostScrap(id: Long): Flow<String> = flow {
        runCatching { communityService.postPostScrap(id) }
            .onSuccess { response ->
                emit(response.message)
            }
            .onFailure {
                Log.d("YOON-CHAN", "CommnunityRepository postPostScrap error ${it.message}")
            }
    }

    override fun postPostScrapMap(id: Long, scrap: Boolean): Flow<Map<Long, Boolean>> = flow {
        postScrapMap = if (postScrapMap.containsKey(id)) {
            postScrapMap - id
            postScrapMap + Pair(id, !scrap)
        } else {
            postScrapMap + Pair(id, !scrap)
        }
        emit(postScrapMap)
    }

    override fun getPostDetail(id: Long): Flow<PostDetail> = flow {
        runCatching { communityService.getPostDetail(id) }
            .onSuccess { response ->
                response.data?.let {
                    emit(it.toData())
                }
            }
            .onFailure {
                Log.d("YOON-CHAN", "CommnunityRepository getPostDetail error ${it.message}")
            }
    }

    override fun getPostDetailComments(id: Long): Flow<List<Comment>> = flow {
        runCatching { commentService.getPostDetailComments(id) }
            .onSuccess { response ->
                response.data?.let { list ->
                    emit(list.map { it.toDate() })
                }
            }
            .onFailure {
                Log.d("YOON-CHAN", "CommnunityRepository getPostDetailComments error ${it.message}")
            }
    }

    override fun getPostScrapMap(): Flow<Map<Long, Boolean>> = flow {
        emit(postScrapMap)
    }

    override fun postCommentLike(id: Long, like: Boolean): Flow<String> = flow {
        runCatching {
            commentService.postLikes(CommentLikeRequest(id, like).toRequestBody())
        }
            .onSuccess { response ->
                emit(response.message)
            }
            .onFailure {
                Log.d("YOON-CHAN", "CommnunityRepository postCommentLike error ${it.message}")
            }
    }

    override fun postAddComment(id: Long, text: String): Flow<Long> = flow {
        runCatching {
            communityService.postPostAddComment(PostAddCommentRequest(id, text).toRequest())
        }
            .onSuccess { response ->
                response.data?.let {
                    emit(it.commentId)
                }
            }
            .onFailure {
                Log.d("YOON-CHAN", "CommnunityRepository postCommentLike error ${it.message}")
            }
    }

    override fun patchComment(id: Long, content: String): Flow<String> = flow {
        runCatching {
            commentService.patchComment(PatchCommentRequest(id, content).toRequestBody())
        }
            .onSuccess { response ->
                emit(response.message)
            }
            .onFailure {
                Log.d("YOON-CHAN", "CommnunityRepository patchComment error ${it.message}")
            }
    }
}
