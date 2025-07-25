package com.youthtalk.repository

import com.core.dataapi.repository.HomeRepository
import com.youthtalk.data.PolicyService
import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.home.HomeDataResponse
import com.youthtalk.mapper.toDomain
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
class HomeRepositoryTest {

    private lateinit var sut: HomeRepository

    @Mock
    private lateinit var policyService: PolicyService

    @Before
    fun setUp() {
        sut = HomeRepositoryImpl(policyService)
    }

    @Test
    fun given_whenGetHomeData_whenReturnsHomeData() {
        runTest {
            // given
            val homeDataResponse = HomeDataResponse(listOf(), listOf(), listOf())
            whenever(policyService.getHome()).thenReturn(CommonResponse(200, "요청에 성공하였습니다", "S01", homeDataResponse))

            // when
            val result = sut.getHome().getOrThrow()

            // then
            assertEquals(homeDataResponse.toDomain(), result)
            verify(policyService).getHome()
        }
    }
}
