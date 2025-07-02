package com.youthtalk.repository

import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.Images
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.core.dataapi.repository.CommunityRepository
import com.core.exception.NoDataException
import com.youthtalk.data.CommunityService
import com.youthtalk.datasource.post.MyPageRemoteMediator
import com.youthtalk.datasource.post.PostRemoteMediator
import com.youthtalk.datasource.room.YouthDatabase
import com.youthtalk.dto.MemberId
import com.youthtalk.mapper.toData
import com.youthtalk.mapper.toDomain
import com.youthtalk.model.Image
import com.youthtalk.model.post.CreatePost
import com.youthtalk.model.post.ModifyPost
import com.youthtalk.model.post.Post
import com.youthtalk.model.post.PostDetail
import com.youthtalk.model.post.PostSubject
import com.youthtalk.model.post.PostType
import com.youthtalk.model.typeenum.Category
import com.youthtalk.utils.ErrorUtils.throwableError
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class CommunityRepositoryImpl @Inject constructor(
    private val communityService: CommunityService,
    private val youthDatabase: YouthDatabase,
    private val context: Context
) : CommunityRepository {
    override fun getPopularPosts(category: Category, postSubject: PostSubject): Flow<List<Post>> = flow {
        val categories = if (category == Category.ALL) {
            Category.entries.filter { it != Category.ALL }.map { it.name }.toList()
        } else {
            listOf(
                category.name
            )
        }
        runCatching {
            when (postSubject) {
                PostSubject.REVIEW -> communityService.getReviewPosts(categories = categories, page = 0, size = 10)
                PostSubject.POST -> communityService.getPosts(page = 0, size = 10)
            }
        }
            .onSuccess { data ->
                Timber.e("CommunityRepositoryImpl getPopularPosts Success $data")
                data.data?.let { postResponse ->
                    emit(postResponse.popularPosts.map { it.toDomain() })
                } ?: throw NoDataException()
            }
            .onFailure { error ->
                Timber.e("CommunityRepositoryImpl getPopularPosts error : $error")
                throwableError<MemberId>(error)
            }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPosts(category: Category, postType: PostType, postSubject: PostSubject): Flow<Flow<PagingData<Post>>> = flow {
        val categories = if (category == Category.ALL) {
            Category.entries.filter { it != Category.ALL }.map { it.name }.toList()
        } else {
            listOf(
                category.name
            )
        }
        emit(
            Pager(
                config = PagingConfig(
                    pageSize = 10,
                    enablePlaceholders = true
                ),
                remoteMediator = PostRemoteMediator(
                    communityService = communityService,
                    categories = categories,
                    postType = postType,
                    postSubject = postSubject,
                    youthDatabase = youthDatabase
                )
            ) {
                youthDatabase.postDao().getPagingSource(postType = postType)
            }.flow
        )
    }

    override fun getListImage(): Flow<List<Image>> = flow {
        val contentResolver = context.contentResolver
        val projection = arrayOf(
            Images.Media._ID,
            Images.Media.DISPLAY_NAME,
            Images.Media.SIZE,
            Images.Media.MIME_TYPE
        )

        val collectionUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            Images.Media.EXTERNAL_CONTENT_URI
        }

        val images = mutableListOf<Image>()

        contentResolver.query(
            collectionUri,
            projection,
            null,
            null,
            "${Images.Media.DATE_ADDED} DESC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(Images.Media._ID)
            val displayNameColumn = cursor.getColumnIndexOrThrow(Images.Media.DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndexOrThrow(Images.Media.SIZE)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(Images.Media.MIME_TYPE)

            while (cursor.moveToNext()) {
                val uri = ContentUris.withAppendedId(collectionUri, cursor.getLong(idColumn))
                val name = cursor.getString(displayNameColumn)
                val size = cursor.getLong(sizeColumn)
                val mimeType = cursor.getString(mimeTypeColumn)

                val image = Image(uri.toString(), name, size, mimeType)
                images.add(image)
            }
        }

        emit(images)
    }.flowOn(Dispatchers.IO)

    override fun postCreatePost(createPost: CreatePost): Flow<Long> = flow {
        runCatching {
            communityService.postCreate(createPost.toData().toRequestBody())
        }
            .onSuccess { response ->
                response.data?.let { post ->
                    emit(post.postId)
                }
            }
            .onFailure {
                Timber.e("postCreatePost error $it")
                throwableError<Long>(it)
            }
    }

    override fun getPostDetail(postId: Long) = flow {
        runCatching {
            communityService.getPostDetail(postId)
        }
            .onSuccess { response ->
                response.data?.let { post ->
                    emit(post.toData())
                }
            }
            .onFailure {
                Timber.e("postCreatePost getPostDetail $it")
                throwableError<PostDetail>(it)
            }
    }

    override fun deletePost(postId: Long): Flow<Long> = flow {
        runCatching {
            communityService.deletePost(postId)
        }
            .onSuccess {
                youthDatabase.postDao().deletePost(postId)
                emit(postId)
            }
            .onFailure {
                Timber.e("postCreatePost deletePost $it")
                throwableError<Long>(it)
            }
    }

    override fun postPostScrap(postId: Long, scrap: Boolean): Flow<Long> = flow {
        runCatching {
            communityService.postPostScrap(postId)
        }
            .onSuccess {
                youthDatabase.postDao().updatePostScrap(postId, !scrap)
                emit(postId)
            }
            .onFailure {
                Timber.e("postCreatePost postPostScrap $it")
                throwableError<Long>(it)
            }
    }

    override fun syncPostScrap(reviews: List<Post>, frees: List<Post>): Flow<Pair<List<Post>, List<Post>>> = flow {
        val syncReviews = reviews
            .map { post ->
                val syncPost = youthDatabase.postDao().getPost(post.postId)?.copy(postType = post.postType)
                Timber.e("repository syncPostScrap $syncPost")
                syncPost ?: post
            }
        val syncFrees = frees
            .map { post -> youthDatabase.postDao().getPost(post.postId)?.copy(postType = post.postType) ?: post }

        emit(Pair(syncReviews, syncFrees))
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getSettingPosts(isScrapType: Boolean): Flow<Flow<PagingData<Post>>> = flow {
        emit(
            Pager(
                config = PagingConfig(
                    pageSize = 10,
                    enablePlaceholders = true
                ),
                remoteMediator = MyPageRemoteMediator(
                    communityService = communityService,
                    postType = PostType.MY_PAGE,
                    youthDatabase = youthDatabase,
                    isScrap = isScrapType
                )
            ) {
                if (isScrapType) {
                    youthDatabase.postDao().getScrapPagingSource(postType = PostType.MY_PAGE)
                } else {
                    youthDatabase.postDao().getPagingSource(postType = PostType.MY_PAGE)
                }
            }.flow
        )
    }

    override fun getSettingPostCount(isScrapType: Boolean): Flow<Int> = flow {
        runCatching {
            if (isScrapType) {
                communityService.getScrapPosts(0, 1)
            } else {
                communityService.getMyPosts(0, 1)
            }
        }
            .onSuccess { response ->
                response.data?.let {
                    emit(it.total)
                }
            }
            .onFailure {
                Timber.e("CommunityRepositoryImpl getSettingPostCount $it")
                throwableError<Int>(it)
            }
    }

    override fun postModifyPost(postId: Long, modifyPost: ModifyPost): Flow<Long> = flow {
        val requestBody = modifyPost.toData().toRequestBody()
        runCatching {
            communityService.postModifyPost(postId, requestBody)
        }
            .onSuccess { response ->
                response.data?.let { postDetail ->
                    youthDatabase.postDao().updateModifyPost(postId, postDetail.title, postDetail.contentList[0].content.split("\n").first())
                    emit(postDetail.postId)
                }
            }
            .onFailure {
                Timber.e("CommunityRepositoryImpl postModifyPost error $it")
                throwableError<Int>(it)
            }
    }
}
