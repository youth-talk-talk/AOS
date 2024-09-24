package com.youthtalk.repository

import com.core.dataapi.repository.PolicyDetailRepository
import com.core.exception.NoDataException
import com.youthtalk.data.PolicyService
import com.youthtalk.dto.PolicyDetailResponse
import com.youthtalk.mapper.toData
import com.youthtalk.model.PolicyDetail
import com.youthtalk.utils.ErrorUtils.throwableError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PolicyDetailRepositoryImpl @Inject constructor(
    private val policyService: PolicyService,
) : PolicyDetailRepository {

    override fun getPolicyDetail(policyId: String): Flow<PolicyDetail> = flow {
        runCatching {
            policyService.getPolicyDetail(policyId)
        }
            .onSuccess { response ->
                response.data?.let {
                    emit(it.toData())
                } ?: throw NoDataException("no Data")
            }
            .onFailure {
                throwableError<PolicyDetailResponse>(it)
            }
    }
}
