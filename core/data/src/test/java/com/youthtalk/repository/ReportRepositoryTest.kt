package com.youthtalk.repository

import com.core.exception.BadRequestException
import com.core.exception.ConflictException
import com.youthtalk.data.ReportService
import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.toResponseBody
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class ReportRepositoryTest {

    @InjectMocks
    private lateinit var sut: ReportRepositoryImpl

    @Mock
    private lateinit var reportService: ReportService

    @Test
    fun givenPostId_whenReportPost_thenWorksFine() {
        runTest {
            // given
            val postId = 23L

            whenever(reportService.reportPosts(postId)).thenReturn(CommonResponse(200, "", "S01", Unit))

            // when
            val result = sut.reportPost(postId).getOrThrow()

            // then
            assertEquals(Unit, result)
            verify(reportService).reportPosts(postId)
        }
    }

    @Test
    fun givenCommentId_whenReportComment_thenWorksFine() {
        runTest {
            // given
            val commentId = 23L

            whenever(reportService.reportComments(commentId)).thenReturn(CommonResponse(200, "", "S01", Unit))

            // when
            val result = sut.reportComment(commentId).getOrThrow()

            // then
            assertEquals(Unit, result)
            verify(reportService).reportComments(commentId)
        }
    }

    @Test
    fun givenAlreadyReportPostId_whenReportPost_thenThrows409Exception() {
        runTest {
            // given
            val reportedPostId = 231L

            whenever(reportService.reportPosts(reportedPostId)).thenThrow(
                HttpException(
                    Response.error<Any>(
                        409,
                        toResponseBody(CommonResponse<Unit?>(409, "이미 신고한 게시글(또는 댓글)입니다.", "R01", null))
                    )
                )
            )

            // when
            Assert.assertThrows(ConflictException::class.java) {
                runBlocking {
                    sut.reportPost(reportedPostId).getOrThrow()
                }
            }

            // then
            verify(reportService).reportPosts(reportedPostId)
        }
    }

    @Test
    fun givenMyPostId_whenReportPost_thenThrows400Exception() {
        runTest {
            // given
            val myPostId = 123L

            whenever(reportService.reportPosts(myPostId)).thenThrow(
                HttpException(
                    Response.error<Any>(
                        400,
                        toResponseBody(CommonResponse<Unit?>(400, "본인의 게시글(또는 댓글)은 신고할 수 없습니다.", "R02", null))
                    )
                )
            )

            // when
            Assert.assertThrows(BadRequestException::class.java) {
                runBlocking {
                    sut.reportPost(myPostId).getOrThrow()
                }
            }

            // then
            verify(reportService).reportPosts(myPostId)
        }
    }

    @Test
    fun givenNotFoundPostId_whenReportPost_thenThrows400Exception() {
        runTest {
            // given
            val notFoundPostId = 123123L

            whenever(reportService.reportPosts(notFoundPostId)).thenThrow(
                HttpException(
                    Response.error<Any>(
                        400,
                        toResponseBody(CommonResponse<Unit?>(400, "해당 게시글을 찾을 수 없습니다.", "PS01", null))
                    )
                )
            )

            // when
            Assert.assertThrows(BadRequestException::class.java) {
                runBlocking {
                    sut.reportPost(notFoundPostId).getOrThrow()
                }
            }

            // then
            verify(reportService).reportPosts(notFoundPostId)
        }
    }
}
