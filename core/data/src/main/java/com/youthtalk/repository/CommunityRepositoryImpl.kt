package com.youthtalk.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.core.dataapi.repository.CommunityRepository
import com.core.exception.NoDataException
import com.youthtalk.data.CommunityService
import com.youthtalk.datasource.post.PostRemoteMediator
import com.youthtalk.datasource.room.YouthDatabase
import com.youthtalk.dto.MemberId
import com.youthtalk.mapper.toDomain
import com.youthtalk.model.post.Post
import com.youthtalk.model.post.PostSubject
import com.youthtalk.model.post.PostType
import com.youthtalk.model.typeenum.Category
import com.youthtalk.utils.ErrorUtils.throwableError
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class CommunityRepositoryImpl @Inject constructor(
    private val communityService: CommunityService,
    private val youthDatabase: YouthDatabase
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
                PostSubject.REVIEW -> communityService.postReviewPosts(categories = categories, page = 0, size = 10)
                PostSubject.FREE -> communityService.getPosts(page = 0, size = 10)
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
}
