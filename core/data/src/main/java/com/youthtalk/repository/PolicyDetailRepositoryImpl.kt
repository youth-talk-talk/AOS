package com.youthtalk.repository

import android.util.Log
import com.core.dataapi.repository.PolicyDetailRepository
import com.core.exception.NoDataException
import com.youthtalk.data.PolicyService
import com.youthtalk.dto.PolicyDetailResponse
import com.youthtalk.mapper.toData
import com.youthtalk.model.PolicyDetail
import com.youthtalk.utils.ErrorUtils.throwableError
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class PolicyDetailRepositoryImpl @Inject constructor(
    private val policyService: PolicyService
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
                Log.e("YOON-CHAN", "getPolicyDetail $it")
                Timber.e("getPolicyDetail $it")
                throwableError<PolicyDetailResponse>(it)
            }
    }
}
