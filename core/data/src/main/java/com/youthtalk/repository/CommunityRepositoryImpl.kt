package com.youthtalk.repository

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
import com.youthtalk.dto.CommentResponse
import com.youthtalk.dto.PostAddCommentResponse
import com.youthtalk.dto.PostResponse
import com.youthtalk.dto.community.PatchCommentRequest
import com.youthtalk.dto.community.PostAddCommentRequest
import com.youthtalk.dto.community.PostContentRequest
import com.youthtalk.dto.community.PostCreatePostRequest
import com.youthtalk.dto.community.PostDetailResponse
import com.youthtalk.dto.community.PostModifyPostRequest
import com.youthtalk.mapper.toData
import com.youthtalk.mapper.toDate
import com.youthtalk.model.Comment
import com.youthtalk.model.Post
import com.youthtalk.model.PostDetail
import com.youthtalk.model.WriteInfo
import com.youthtalk.utils.ErrorUtils.throwableError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class CommunityRepositoryImpl @Inject constructor(
    private val communityService: CommunityService,
    private val commentService: CommentService,
    private val dataStoreDataSource: DataStoreDataSource,
) : CommunityRepository {
    companion object {
        var postScrapMap: Map<Long, Boolean> = mapOf()
    }

    override fun postReviewPost(): Flow<PagingData<Post>> = Pager(
        config = PagingConfig(
            pageSize = 50,
            prefetchDistance = 2,
        ),
        pagingSourceFactory = {
            ReviewPostPagingSource(
                communityService = communityService,
                dataSource = dataStoreDataSource,
            )
        },
    ).flow

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
                throwableError<PostResponse>(it)
            }
    }

    override fun getPosts(): Flow<PagingData<Post>> = Pager(
        config = PagingConfig(
            pageSize = 50,
            prefetchDistance = 2,
        ),
        pagingSourceFactory = {
            PostPagingSource(
                communityService = communityService,
            )
        },
    ).flow

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
                throwableError<PostResponse>(it)
            }
    }

    override fun postPostScrap(id: Long): Flow<String> = flow {
        runCatching { communityService.postPostScrap(id) }
            .onSuccess { response ->
                emit(response.message)
            }
            .onFailure {
                throwableError<Unit>(it)
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
                throwableError<PostDetailResponse>(it)
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
                throwableError<List<CommentResponse>>(it)
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
                throwableError<Unit>(it)
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
                throwableError<PostAddCommentResponse>(it)
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
                throwableError<Unit>(it)
            }
    }

    override fun uploadImage(file: File): Flow<String> = flow {
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", file.name, requestBody)
        runCatching {
            communityService.postUploadImage(imagePart)
        }
            .onSuccess { response ->
                response.data?.let { uri ->
                    emit(uri)
                }
            }
            .onFailure {
                throwableError<String>(it)
            }
    }

    override fun postCreate(postType: String, title: String, content: List<WriteInfo>, policyId: String?): Flow<PostDetail> = flow {
        val contentList = mutableListOf<PostContentRequest>()
        content.forEach {
            it.uri?.let { uri ->
                contentList.add(PostContentRequest(uri, "IMAGE"))
            }
            contentList.add(PostContentRequest(it.content ?: "", "TEXT"))
        }
        val requestBody = PostCreatePostRequest(
            postType = postType,
            title = title,
            policyId = policyId,
            contentList = contentList.toList(),
        ).toRequestBody()

        runCatching { communityService.postCreate(requestBody) }
            .onSuccess { response ->
                response.data?.let { data ->
                    emit(data.toData())
                }
            }
            .onFailure {
                throwableError<PostDetailResponse>(it)
            }
    }

    override fun postModifyPost(postId: Long, postType: String, title: String, content: List<WriteInfo>, policyId: String?): Flow<PostDetail> = flow {
        val contentList = mutableListOf<PostContentRequest>()
        content.forEach {
            it.uri?.let { uri ->
                contentList.add(PostContentRequest(uri, "IMAGE"))
            }
            contentList.add(PostContentRequest(it.content ?: "", "TEXT"))
        }
        val requestBody = PostModifyPostRequest(
            title = title,
            postType = postType,
            policyId = policyId,
            contentList = contentList,
            addImgUrlList = listOf(),
            deletedImgUrlList = listOf(),
        ).toRequestBody()
        runCatching {
            communityService.postModifyPost(
                id = postId,
                requestBody = requestBody,
            )
        }
            .onSuccess { response ->
                response.data?.let { data ->
                    emit(data.toData())
                }
            }
            .onFailure {
                throwableError<PostDetailResponse>(it)
            }
    }
}
