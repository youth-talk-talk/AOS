package com.youthtalk.repository

import com.core.dataapi.repository.LoginRepository
import com.core.datastore.datasource.DataStoreDataSource
import com.core.exception.NoDataException
import com.youthtalk.data.LoginService
import com.youthtalk.dto.LoginRequest
import com.youthtalk.dto.MemberId
import com.youthtalk.dto.SignRequest
import com.youthtalk.model.toRegion
import com.youthtalk.utils.ErrorUtils.throwableError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginService: LoginService,
    private val dataStoreDataSource: DataStoreDataSource,
) : LoginRepository {
    override fun postLogin(socialId: String): Flow<Long> = flow {
        Timber.e("LoginRepositoryImpl postLogin start")
        runCatching { loginService.postLogin(LoginRequest(socialType = "kakao", socialId = socialId).toRequestBody()) }
            .onSuccess { token ->
                Timber.e("LoginRepositoryImpl postLogin Success $token")
                token.data?.let { data ->
                    emit(data.memberId)
                } ?: throw NoDataException()
            }
            .onFailure { error ->
                Timber.e("LoginRepositoryImpl postLogin  error : $error")
                throwableError<MemberId>(error)
            }
    }

    override fun hasToken(): Flow<Boolean> = dataStoreDataSource.hasToken()

    override fun postSign(id: String, nickname: String, region: String): Flow<Int> = flow {
        Timber.e("LoginRepositoryImpl start")
        runCatching {
            loginService.postSignUp(
                SignRequest(
                    socialType = "kakao",
                    socialId = id,
                    nickname = nickname,
                    region = region.toRegion().region,
                ).toRequestBody(),
            )
        }
            .onSuccess { response ->
                Timber.e("LoginRepositoryImpl Success $response")
                response.data?.let {
                    emit(it)
                } ?: throw NoDataException()
            }
            .onFailure { error ->
                Timber.e("LoginRepositoryImpl error : $error")
                throwableError<Int>(error)
            }
    }
}
