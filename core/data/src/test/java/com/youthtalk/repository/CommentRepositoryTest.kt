package com.youthtalk.repository

import com.core.exception.BadRequestException
import com.youthtalk.data.CommentService
import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.PostAddCommentResponse
import com.youthtalk.dto.comment.CommentInfoResponse
import com.youthtalk.dto.comment.CommentResponse
import com.youthtalk.dto.toResponseBody
import com.youthtalk.mapper.toData
import com.youthtalk.model.comment.CommentInfo
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class CommentRepositoryTest {

    @InjectMocks
    private lateinit var sut: CommentRepositoryImpl

    @Mock
    private lateinit var commentService: CommentService

    @Test
    fun givenPolicyId_whenGetPolicyComment_thenReturnsComments() {
        runTest {
            // given
            val policyId = 1L
            val commentResponse = CommentInfoResponse(1, listOf(CommentResponse(1L, 1L, "asd", "asd", true, null, "2025-05-03 20:31:23")))
            val comments = commentResponse.toData()
            whenever(commentService.getPolicyComment(policyId)).thenReturn(CommonResponse(200, "요청에 성공하였습니다", "S01", commentResponse))

            // when
            val result = sut.getPolicyComment(policyId).getOrThrow()

            // then
            assertEquals(comments, result)
            verify(commentService).getPolicyComment(policyId)
        }
    }

    @Test
    fun givenPolicyId_whenGetPolicyComment_thenReturnsEmptyComments() {
        runTest {
            // given
            val policyId = 2L
            val emptyComment = CommentInfo(0, listOf())
            whenever(commentService.getPolicyComment(policyId)).thenReturn(CommonResponse(200, "해당하는 댓글이 없습니다.", "S09", null))

            // when
            val result = sut.getPolicyComment(policyId).getOrThrow()

            // then
            assertEquals(emptyComment, result)
            verify(commentService).getPolicyComment(policyId)
        }
    }

    @Test
    fun givenNotFoundPolicyId_whenGetPolicyComment_thenThrows400Exception() {
        runTest {
            // given
            val policyId = 123L
            whenever(commentService.getPolicyComment(policyId)).thenThrow(
                HttpException(
                    Response.error<Any>(
                        400,
                        toResponseBody(CommonResponse<Unit?>(400, "해당 정책을 찾을 수 없습니다.", "PC01", null))
                    )
                )
            )

            // when
            assertThrows(BadRequestException::class.java) {
                runBlocking {
                    sut.getPolicyComment(policyId)
                        .onFailure {
                            assertEquals("해당 정책을 찾을 수 없습니다.", it.message)
                        }.getOrThrow()
                }
            }

            // then
            verify(commentService).getPolicyComment(policyId)
        }
    }

    @Test
    fun givenNotValidPolicyId_whenGetPolicyComment_thenThrows400Exception() {
        runTest {
            // given
            val policyId = 1231321L
            whenever(commentService.getPolicyComment(policyId)).thenThrow(
                HttpException(
                    Response.error<Any>(
                        400,
                        toResponseBody(CommonResponse<Unit?>(400, "유효하지 않은 값을 입력하였습니다.", "F01", null))
                    )
                )
            )

            // when
            assertThrows(BadRequestException::class.java) {
                runBlocking {
                    sut.getPolicyComment(policyId)
                        .onFailure {
                            assertEquals("유효하지 않은 값을 입력하였습니다.", it.message)
                        }.getOrThrow()
                }
            }

            // then
            verify(commentService).getPolicyComment(policyId)
        }
    }

    @Test
    fun givenPostId_whenGetPostComment_thenReturnsComments() {
        runTest {
            // given
            val postId = 1L
            val commentResponse = CommentInfoResponse(1, listOf(CommentResponse(1L, 1L, "asd", "asd", true, null, "2025-05-03 20:31:23")))
            val comments = commentResponse.toData()
            whenever(commentService.getPostDetailComments(postId)).thenReturn(CommonResponse(200, "요청에 성공하였습니다", "S01", commentResponse))

            // when
            val result = sut.getPostDetailComments(postId).getOrThrow()

            // then
            assertEquals(comments, result)
            verify(commentService).getPostDetailComments(postId)
        }
    }

    @Test
    fun givenPostId_whenPostComment_thenReturnsCommentId() {
        runTest {
            // given
            val postId = 3L
            val commentId = 123L

            whenever(
                commentService.postPostAddComment(any())
            ).thenReturn(CommonResponse(200, "댓글을 성공적으로 등록했습니다.", "S06", PostAddCommentResponse(commentId)))

            // when
            val result = sut.postPostAddComment(postId, "댓글 내용").getOrThrow()

            // when
            assertEquals(commentId, result)
            verify(commentService).postPostAddComment(any())
        }
    }
}
