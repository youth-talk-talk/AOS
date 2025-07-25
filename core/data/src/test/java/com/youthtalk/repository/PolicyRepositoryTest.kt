package com.youthtalk.repository

import com.core.dataapi.repository.PolicyRepository
import com.youthtalk.data.PolicyService
import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.PolicyDetailResponse
import com.youthtalk.mapper.toData
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
class PolicyRepositoryTest {

    private lateinit var sut: PolicyRepository

    @Mock
    private lateinit var policyService: PolicyService

    @Before
    fun setUp() {
        sut = PolicyRepositoryImpl(policyService)
    }

    @Test
    fun givenPolicyId_whenGetPolicyDetail_thenReturnsPolicy() {
        runTest {
            // given
            val policyId = 3L
            val policyDetailResponse = createPolicyDetailResponse()

            whenever(policyService.getPolicyDetail(policyId)).thenReturn(CommonResponse(200, "정책 조회에 성공하였습니다.", "S04", policyDetailResponse))

            // when
            val result = sut.getPolicyDetail(policyId).getOrThrow()

            // then
            assertEquals(policyDetailResponse.toData(), result)
            verify(policyService).getPolicyDetail(policyId)
        }
    }

    private fun createPolicyDetailResponse(): PolicyDetailResponse {
        return PolicyDetailResponse("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", true, "", "")
    }
}
