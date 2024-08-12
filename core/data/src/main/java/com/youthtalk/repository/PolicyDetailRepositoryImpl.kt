package com.youthtalk.repository

import android.util.Log
import com.core.dataapi.repository.PolicyDetailRepository
import com.core.exception.NoDataException
import com.youthtalk.data.PolicyService
import com.youthtalk.mapper.toData
import com.youthtalk.model.PolicyDetail
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
                Log.d("YOON-CHAN", "PolicyDetail error ${it.message}")
            }
    }
}
