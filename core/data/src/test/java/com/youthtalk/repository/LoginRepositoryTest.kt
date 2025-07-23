package com.youthtalk.repository

import com.core.datastore.datasource.DataStoreDataSource
import com.core.exception.BadRequestException
import com.core.exception.UnAuthorizedException
import com.youthtalk.data.LoginService
import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.MemberId
import com.youthtalk.dto.login.LoginRequest
import com.youthtalk.dto.login.SignRequest
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
        runTest {
            // given
            val socialId = "88888"
            val memberId = 23L
            val loginRequest = LoginRequest(socialType = "kakao", socialId = socialId)

            whenever(loginService.postLogin(any())).thenReturn(CommonResponse(200, "요청에 성공하였습니다", "S01", MemberId(memberId)))

            // when
            val result = sut.postLogin(socialId).getOrThrow()

            // then
            assertEquals(memberId, result)
            verify(loginService).postLogin(any())
        }
    }

    @Test
    fun givenWrongSocialId_whenLogin_thenThrowsNull() {
        runTest {
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

    @Test
    fun givenSignInfo_whenSignUp_thenReturnsMemberId() {
        runTest {
            val socialId = "666666"
            val socialType = "kakao"
            val nickname = "압도적도적"
            val region = "서울"
            val memberId = 23

            val signUpRequest = SignRequest(socialType, socialId, nickname, region)
            whenever(loginService.postSignUp(any())).thenReturn(CommonResponse(200, "요청에 성공하였습니다", "S01", memberId))

            // when
            val result = sut.postSign(socialId, nickname, region).getOrThrow()

            // then
            assertEquals(memberId, result)
            verify(loginService).postSignUp(any())
        }
    }

    @Test
    fun givenWrongRegion_whenSignUp_thenThrows400Exception() {
        runTest {
            val socialId = "666666"
            val socialType = "kakao"
            val nickname = "압도적도적"
            val region = "없는지역"
            val memberId = 23

            whenever(loginService.postSignUp(any())).thenThrow(
                HttpException(
                    Response.error<Any>(
                        400,
                        toResponseBody(CommonResponse<ArrayList<String>>(400, "유효하지 않은 값을 입력하였습니다.", "F01", arrayListOf("지역이 유효하지 않습니다.")))
                    )
                )
            )

            // when
            Assert.assertThrows(BadRequestException::class.java) {
                runBlocking {
                    sut.postSign(socialId, nickname, region).getOrThrow()
                }
            }

            // then
            verify(loginService).postSignUp(any())
        }
    }
}
