package com.youthtalk.repository

import com.core.datastore.datasource.DataStoreDataSource
import com.core.exception.UnAuthorizedException
import com.youthtalk.data.LoginService
import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.MemberId
import com.youthtalk.dto.login.LoginRequest
import com.youthtalk.dto.toResponseBody
import kotlinx.coroutines.runBlocking
import org.junit.Assert
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
class LoginRepositoryTest {

    @InjectMocks
    private lateinit var sut: LoginRepositoryImpl

    @Mock
    private lateinit var loginService: LoginService

    @Mock
    private lateinit var dataStoreDataSource: DataStoreDataSource

    @Test
    fun givenSocialId_whenLogin_thenReturnsMemberId() {
        runBlocking {
            // given
            val socialId = "88888"
            val memberId = 23L
            val loginRequest = LoginRequest(socialType = "kakao", socialId = socialId)

            whenever(loginService.postLogin(any())).thenReturn(CommonResponse(200, "요청에 성공하였습니다", "S01", MemberId(memberId)))

            // when
            val result = sut.postLogin(socialId).getOrThrow()

            // then
            Assert.assertEquals(memberId, result)
            verify(loginService).postLogin(any())
        }
    }

    @Test
    fun givenWrongSocialId_whenLogin_thenThrowsNull() {
        runBlocking {
            // given
            val socialId = "xxxxxx"
            val memberId = null
            val loginRequest = LoginRequest(socialType = "kakao", socialId = socialId)

            whenever(loginService.postLogin(any())).thenThrow(
                HttpException(
                    Response.error<Any>(
                        401,
                        toResponseBody(CommonResponse<Unit?>(401, "회원이 아닙니다.", "F01", memberId))
                    )
                )
            )

            // when
            Assert.assertThrows(UnAuthorizedException::class.java) {
                runBlocking {
                    sut.postLogin(socialId).getOrThrow()
                }
            }

            // then
            verify(loginService).postLogin(any())
        }
    }
}
