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
import com.youthtalk.mapper.toData
import com.youthtalk.mapper.toDomain
import com.youthtalk.model.Image
import com.youthtalk.model.post.CreatePost
import com.youthtalk.model.post.ModifyPost
import com.youthtalk.model.post.Post
import com.youthtalk.model.post.PostSubject
import com.youthtalk.model.post.PostType
import com.youthtalk.model.typeenum.Category
import com.youthtalk.utils.ErrorUtils.createResult
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import timber.log.Timber

class CommunityRepositoryImpl @Inject constructor(
    private val communityService: CommunityService,
    private val youthDatabase: YouthDatabase,
    private val context: Context
) : CommunityRepository {
    override suspend fun getPopularPosts(category: Category, postSubject: PostSubject): Result<List<Post>> = createResult {
        val categories = if (category == Category.ALL) {
            Category.entries.filter { it != Category.ALL }.map { it.name }.toList()
        } else {
            listOf(
                category.name
            )
        }
        when (postSubject) {
            PostSubject.REVIEW -> communityService.getReviewPosts(categories = categories, page = 0, size = 10)
            PostSubject.POST -> communityService.getPosts(page = 0, size = 10)
        }.data?.popularPosts?.map { it.toDomain() } ?: throw NoDataException()
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPosts(category: Category, postType: PostType, postSubject: PostSubject): Flow<PagingData<Post>> {
        val categories = if (category == Category.ALL) {
            Category.entries.filter { it != Category.ALL }.map { it.name }.toList()
        } else {
            listOf(
                category.name
            )
        }
        return Pager(
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
    }

    override suspend fun getListImage(): Result<List<Image>> = runCatching {
        withContext(Dispatchers.IO) {
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
            images
        }
    }

    override suspend fun postCreatePost(createPost: CreatePost): Result<Long> = createResult {
        communityService.postCreate(createPost.toData().toRequestBody()).data?.postId ?: throw NoDataException()
    }

    override suspend fun getPostDetail(postId: Long) = createResult {
        communityService.getPostDetail(postId).data?.toData() ?: throw NoDataException()
    }

    override suspend fun deletePost(postId: Long): Result<Long> = createResult {
        communityService.deletePost(postId)
        youthDatabase.postDao().deletePost(postId)
        postId
    }

    override suspend fun postPostScrap(postId: Long, scrap: Boolean): Result<Long> = createResult {
        communityService.postPostScrap(postId)
        youthDatabase.postDao().updatePostScrap(postId, !scrap)
        postId
    }

    override suspend fun syncPostScrap(reviews: List<Post>, frees: List<Post>): Result<Pair<List<Post>, List<Post>>> = createResult {
        val syncReviews = reviews
            .map { post ->
                val syncPost = youthDatabase.postDao().getPost(post.postId)?.copy(postType = post.postType)
                Timber.e("repository syncPostScrap $syncPost")
                syncPost ?: post
            }
        val syncFrees = frees
            .map { post -> youthDatabase.postDao().getPost(post.postId)?.copy(postType = post.postType) ?: post }

        Pair(syncReviews, syncFrees)
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getSettingPosts(isScrapType: Boolean): Flow<PagingData<Post>> {
        return Pager(
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
    }

    override suspend fun getSettingPostCount(isScrapType: Boolean): Result<Int> = createResult {
        if (isScrapType) {
            communityService.getScrapPosts(0, 1)
        } else {
            communityService.getMyPosts(0, 1)
        }.data?.total ?: throw NoDataException()
    }

    override suspend fun postModifyPost(postId: Long, modifyPost: ModifyPost): Result<Long> = createResult {
        val requestBody = modifyPost.toData().toRequestBody()

        val postDetail = communityService.postModifyPost(postId, requestBody).data ?: throw NoDataException()
        youthDatabase.postDao().updateModifyPost(postId, postDetail.title, postDetail.contentList[0].content.split("\n").first())
        postDetail.postId
    }
}
