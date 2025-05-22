package com.youthtalk.repository

import com.core.dataapi.repository.PolicyRepository
import com.core.exception.NoDataException
import com.youthtalk.data.PolicyService
import com.youthtalk.dto.PolicyDetailResponse
import com.youthtalk.dto.policy.PolicyResponse
import com.youthtalk.mapper.toData
import com.youthtalk.mapper.toDomain
import com.youthtalk.model.policy.Policy
import com.youthtalk.model.policy.PolicyDetail
import com.youthtalk.utils.ErrorUtils.throwableError
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class PolicyRepositoryImpl @Inject constructor(
    private val policyService: PolicyService
) : PolicyRepository {

    override fun getPolicyDetail(policyId: Long): Flow<PolicyDetail> = flow {
        runCatching {
            policyService.getPolicyDetail(policyId)
        }
            .onSuccess { response ->
                response.data?.let {
                    emit(it.toData())
                } ?: throw NoDataException("no Data")
            }
            .onFailure {
                Timber.e("getPolicyDetail $it")
                throwableError<PolicyDetailResponse>(it)
            }
    }

    override fun getRecentlyViewPolicies(): Flow<List<Policy>> = flow {
        runCatching {
            policyService.getRecentlyViewPolicies()
        }
            .onSuccess { response ->
                response.data?.let { data ->
                    emit(data.map { it.toDomain() })
                } ?: throw NoDataException("no Data")
            }
            .onFailure {
                Timber.e("PolicyRepositoryImpl getRecentlyViewPolicies error $it")
                throwableError<PolicyResponse>(it)
            }
    }
}
