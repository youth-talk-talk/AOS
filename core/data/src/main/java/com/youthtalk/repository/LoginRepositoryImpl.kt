package com.youthtalk.repository

import android.util.Log
import com.core.dataapi.repository.LoginRepository
import com.core.exception.UnAuthorizedException
import com.google.gson.Gson
import com.youthtalk.data.LoginService
import com.youthtalk.mapper.toData
import com.youthtalk.model.CommonResponse
import com.youthtalk.model.LoginRequest
import com.youthtalk.model.Token
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginService: LoginService,
) : LoginRepository {
    override fun postLogin(userId: String): Flow<Token> = flow {
        runCatching { loginService.postLogin(LoginRequest(userId).toRequestBody()) }
            .onSuccess { token ->
                Log.d("YOON-CHAN", "success $token")
                emit(token.data.toData())
            }
            .onFailure { error ->
                when (error) {
                    is HttpException -> {
                        val e = Gson().fromJson(error.response()?.errorBody()?.string(), CommonResponse::class.java)

                        when (e.status) {
                            401 -> throw UnAuthorizedException(e.message)
                        }
                        Log.d("YOON-CHAN", "retrofit2 failure error body")
                        Log.d("YOON-CHAN", "status ${e.status}, code ${e.code}, message ${e.message}, data ${e.data}")
                    }
                }
            }
    }
}
