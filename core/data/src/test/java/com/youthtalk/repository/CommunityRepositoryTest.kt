package com.youthtalk.repository

import android.content.Context
import com.core.dataapi.repository.CommunityRepository
import com.youthtalk.data.CommunityService
import com.youthtalk.datasource.room.YouthDatabase
import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.PostResponse
import com.youthtalk.model.post.Post
import com.youthtalk.model.post.PostSubject
import com.youthtalk.model.typeenum.Category
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class CommunityRepositoryTest {

    private lateinit var sut: CommunityRepository

    @Mock
    private lateinit var communityService: CommunityService

    @Mock
    private lateinit var youthDatabase: YouthDatabase

    @Mock
    private lateinit var context: Context

    @Before
    fun setUp() {
        sut = CommunityRepositoryImpl(communityService, youthDatabase, context)
    }

    @Test
    fun given_whenGetPopularPost_thenReturnsPosts() {
        runTest {
            // given
            val page = 0
            val size = 10
            whenever(communityService.getPosts(page, size)).thenReturn(CommonResponse(200, "요청에 성공하였습니다", "S01", PostResponse(listOf(), listOf())))

            // when
            val result = sut.getPopularPosts(Category.JOB, PostSubject.POST).getOrThrow()

            // then
            assertEquals(emptyList<Post>(), result)
            verify(communityService).getPosts(page, size)
        }
    }

    @Test
    fun given_whenGetPopularReview_thenReturnsReviews() {
        runTest {
            // given
            val page = 0
            val size = 10
            whenever(communityService.getReviewPosts(page, size, listOf(Category.JOB.name))).thenReturn(
                CommonResponse(
                    200,
                    "요청에 성공하였습니다",
                    "S01",
                    PostResponse(
                        listOf(),
                        listOf()
                    )
                )
            )

            // when
            val result = sut.getPopularPosts(Category.JOB, PostSubject.REVIEW).getOrThrow()

            // then
            assertEquals(emptyList<Post>(), result)
            verify(communityService).getReviewPosts(page, size, listOf(Category.JOB.name))
        }
    }
}
