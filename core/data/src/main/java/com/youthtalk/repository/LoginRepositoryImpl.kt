package com.youthtalk.repository

import android.util.Log
import com.core.dataapi.repository.LoginRepository
import com.core.datastore.datasource.DataStoreDataSource
import com.core.exception.InvalidValueException
import com.core.exception.NetworkErrorException
import com.core.exception.NoDataException
import com.core.exception.UnAuthorizedException
import com.youthtalk.data.LoginService
import com.youthtalk.dto.CommonResponse
import com.youthtalk.dto.LoginRequest
import com.youthtalk.dto.SignErrorResponse
import com.youthtalk.dto.SignRequest
import com.youthtalk.model.toRegion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginService: LoginService,
    private val dataStoreDataSource: DataStoreDataSource,
) : LoginRepository {
    override fun postLogin(userId: String): Flow<Long> = flow {
        runCatching { loginService.postLogin(LoginRequest(userId).toRequestBody()) }
            .onSuccess { token ->
                Log.d("YOON-CHAN", "success $token")
                token.data?.let { data ->
                    emit(data.memberId)
                } ?: throw NoDataException("not response Data")
            }
            .onFailure { error ->
                when (error) {
                    is HttpException -> {
                        Log.d("YOON-CHAN", "retrofit2 failure error body")
                        val e = Json.decodeFromString<CommonResponse<Unit>>(error.response()?.errorBody()?.string() ?: "")

                        when (error.code()) {
                            401 -> throw UnAuthorizedException(e.message)
                        }
                        Log.d("YOON-CHAN", "status ${error.code()}")
                    }
                    else -> throw NetworkErrorException("network Error")
                }
            }
    }

    override fun hasToken(): Flow<Boolean> = dataStoreDataSource.hasToken()

    override fun postSign(id: String, nickname: String, region: String): Flow<Int> = flow {
        runCatching { loginService.postSignUp(SignRequest(id, nickname, region.toRegion().region).toRequestBody()) }
            .onSuccess { response ->
                response.data?.let {
                    emit(it)
                } ?: throw NoDataException("not response Data")
            }
            .onFailure { error ->
                when (error) {
                    is HttpException -> {
                        val e = Json.decodeFromString<CommonResponse<SignErrorResponse>>(
                            error.response()?.errorBody()?.string() ?: "",
                        )

                        when (error.code()) {
                            400 -> throw InvalidValueException("${e.status} ${e.data?.messages?.toString()}")
                        }
                    }
                    else -> throw NetworkErrorException("network Error")
                }
            }
    }
}
