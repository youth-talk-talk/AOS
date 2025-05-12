package com.youthtalk.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.core.dataapi.repository.CommunityRepository
import com.core.datastore.datasource.DataStoreDataSource
import com.core.exception.NoDataException
import com.youthtalk.data.CommentService
import com.youthtalk.data.CommunityService
import com.youthtalk.datasource.post.PostRemoteMediator
import com.youthtalk.datasource.review.ReviewPostRemoteMediator
import com.youthtalk.datasource.room.YouthDatabase
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
import com.youthtalk.mapper.toReviewData
import com.youthtalk.model.Comment
import com.youthtalk.model.Post
import com.youthtalk.model.PostDetail
import com.youthtalk.model.PostType
import com.youthtalk.model.ReviewPost
import com.youthtalk.model.WriteInfo
import com.youthtalk.utils.ErrorUtils.throwableError
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber

class CommunityRepositoryImpl @Inject constructor(
    private val communityService: CommunityService,
    private val commentService: CommentService,
    private val youthDatabase: YouthDatabase,
    private val dataStoreDataSource: DataStoreDataSource
) : CommunityRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun postReviewPost(): Flow<PagingData<ReviewPost>> = Pager(
        config = PagingConfig(
            pageSize = 50,
            prefetchDistance = 2
        ),
        remoteMediator = ReviewPostRemoteMediator(
            communityService = communityService,
            youthDatabase = youthDatabase,
            dataSource = dataStoreDataSource
        )
    ) {
        youthDatabase.reviewPostDao().getPagingSource()
    }.flow

    override fun postPopularReviewPost(): Flow<List<ReviewPost>> = flow {
        val categories = dataStoreDataSource.getReviewCategoryFilter().first().map { it.name }
        runCatching {
            communityService.postReviewPosts(
                categories = categories,
                size = 0,
                page = 0
            )
        }
            .onSuccess { response ->
                response.data?.let { popularReviewPosts ->
                    emit(popularReviewPosts.popularPosts.map { it.toReviewData() })
                } ?: throw NoDataException("no Data")
            }
            .onFailure {
                throwableError<PostResponse>(it)
            }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPosts(): Flow<PagingData<Post>> = Pager(
        config = PagingConfig(
            pageSize = 20
        ),
        remoteMediator = PostRemoteMediator(
            communityService = communityService,
            youthDatabase = youthDatabase
        )
    ) {
        youthDatabase.postDao().getPagingSource()
    }.flow

    override fun getPopularPosts(): Flow<List<Post>> = flow {
        runCatching {
            communityService.getPosts(
                size = 0,
                page = 0
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

    override fun postPostScrap(id: Long, scrap: Boolean, type: PostType): Flow<String> = flow {
        runCatching { communityService.postPostScrap(id) }
            .onSuccess { response ->
                setDatabase(id, scrap, type)
                emit(response.message)
            }
            .onFailure {
                throwableError<Unit>(it)
            }
    }

    private suspend fun setDatabase(id: Long, scrap: Boolean, type: PostType) {
        when (type) {
            PostType.REVIEW -> youthDatabase.reviewPostDao().getPostById(id)?.let { reviewPost ->
                youthDatabase.reviewPostDao().updatePost(
                    reviewPost.copy(
                        scrap = !scrap,
                        scraps = reviewPost.scraps + if (!scrap) 1 else -1
                    )
                )
            }

            PostType.POST -> youthDatabase.postDao().getPostById(id)?.let { post ->
                youthDatabase.postDao().updatePost(
                    post.copy(
                        scrap = !scrap,
                        scraps = post.scraps + if (!scrap) 1 else -1
                    )
                )
            }
        }

        youthDatabase.scrapPostDao().getPostById(id)?.let { scrapPost ->
            youthDatabase.scrapPostDao().updatePost(
                scrapPost.copy(
                    scrap = !scrap,
                    scraps = scrapPost.scraps + if (!scrap) 1 else -1
                )
            )
        }
    }

    override fun getPostDetail(id: Long): Flow<PostDetail> = flow {
        runCatching { communityService.getPostDetail(id) }
            .onSuccess { response ->
                response.data?.let {
                    emit(it.toData())
                }
            }
            .onFailure {
                Timber.e("getPostDetail error $it")
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
            contentList = contentList.toList()
        ).toRequestBody()

        runCatching { communityService.postCreate(requestBody) }
            .onSuccess { response ->
                response.data?.let { data ->
                    if (postType == "review") {
                        val reviewPost = ReviewPost(
                            postId = data.postId,
                            title = data.title,
//                            content = data.content,
                            writerId = data.writerId,
                            scraps = 0,
                            scrap = false,
                            comments = 0,
                            policyId = data.policyId,
                            policyTitle = data.policyTitle
                        )
                        youthDatabase.reviewPostDao().insertAll(listOf(reviewPost))
                    } else {
                        val post = Post(
                            postId = data.postId,
                            title = data.title,
//                            content = data.content,
                            writerId = data.writerId,
                            scraps = 0,
                            scrap = false,
                            comments = 0,
                            policyId = data.policyId,
                            policyTitle = data.policyTitle
                        )
                        youthDatabase.postDao().insertAll(listOf(post))
                    }

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
            deletedImgUrlList = listOf()
        ).toRequestBody()
        runCatching {
            communityService.postModifyPost(
                id = postId,
                requestBody = requestBody
            )
        }
            .onSuccess { response ->
                response.data?.let { data ->
                    if (postType == "review") {
                        youthDatabase.reviewPostDao().getPostById(data.postId)?.let { reviewPost ->
                            youthDatabase.reviewPostDao().updatePost(
                                reviewPost.copy(
                                    title = data.title,
                                    policyId = data.policyId,
                                    writerId = data.writerId,
                                    policyTitle = data.policyTitle
//                                    content = data.content,
                                )
                            )
                        }
                    } else {
                        youthDatabase.postDao().getPostById(data.postId)?.let { post ->
                            youthDatabase.postDao().updatePost(
                                post.copy(
                                    title = data.title,
                                    policyId = data.policyId,
                                    writerId = data.writerId,
                                    policyTitle = data.policyTitle
//                                    content = data.content,
                                )
                            )
                        }
                    }
                    emit(data.toData())
                }
            }
            .onFailure {
                throwableError<PostDetailResponse>(it)
            }
    }

    override fun postScrapPost(id: Long): Flow<Long> = flow {
        youthDatabase.scrapPostDao().deletePost(id)
        emit(id)
    }

    override fun deletePost(postId: Long): Flow<String> = flow {
        runCatching {
            communityService.deletePost(postId)
        }
            .onSuccess { response ->
                youthDatabase.scrapPostDao().deletePost(postId)
                youthDatabase.postDao().deletePost(postId)
                youthDatabase.reviewPostDao().deletePost(postId)
                emit(response.message)
            }
            .onFailure {
                throwableError<PostDetailResponse>(it)
            }
    }
}
