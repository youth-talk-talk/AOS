package com.youthtalk.repository

import com.youthtalk.data.ReportService
import com.youthtalk.dto.CommonResponse
import kotlinx.coroutines.runBlocking
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
        runBlocking {
            // given
            val postId = 23L

            whenever(reportService.reportPosts(postId)).thenReturn(CommonResponse(200, "", "S01", Unit))

            // when
            val result = sut.reportPost(postId).getOrThrow()

            // then
            verify(reportService).reportPosts(postId)
        }
    }

    @Test
    fun givenCommentId_whenReportComment_thenWorksFine() {
        runBlocking {
            // given
            val commentId = 23L

            whenever(reportService.reportComments(commentId)).thenReturn(CommonResponse(200, "", "S01", Unit))

            // when
            val result = sut.reportComment(commentId).getOrThrow()

            // then
            verify(reportService).reportComments(commentId)
        }
    }
}
