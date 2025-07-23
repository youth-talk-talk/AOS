package com.youthtalk.repository

import com.youthtalk.data.ReportService
import com.youthtalk.dto.CommonResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

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
}
