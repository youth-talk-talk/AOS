package com.youthtalk.repository

import com.core.dataapi.repository.PolicyRepository
import com.core.exception.NoDataException
import com.youthtalk.data.PolicyService
import com.youthtalk.dto.policy.PolicyResponse
import com.youthtalk.mapper.toData
import com.youthtalk.mapper.toDomain
import com.youthtalk.model.policy.Policy
import com.youthtalk.model.policy.PolicyDetail
import com.youthtalk.utils.ErrorUtils.createResult
import com.youthtalk.utils.ErrorUtils.throwableError
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class PolicyRepositoryImpl @Inject constructor(
    private val policyService: PolicyService
) : PolicyRepository {

    override suspend fun getPolicyDetail(policyId: Long): Result<PolicyDetail> = createResult {
        policyService.getPolicyDetail(policyId).data?.toData() ?: throw NoDataException()
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
