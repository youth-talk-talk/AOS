package com.youthtalk.repository

import com.core.dataapi.repository.LoginRepository
import com.core.datastore.datasource.DataStoreDataSource
import com.core.exception.NoDataException
import com.youthtalk.data.LoginService
import com.youthtalk.dto.login.LoginRequest
import com.youthtalk.dto.login.SignRequest
import com.youthtalk.model.typeenum.toRegion
import com.youthtalk.utils.ErrorUtils.createResult
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class LoginRepositoryImpl @Inject constructor(
    private val loginService: LoginService,
    private val dataStoreDataSource: DataStoreDataSource
) : LoginRepository {
    override suspend fun postLogin(socialId: String): Result<Long> = createResult {
        loginService.postLogin(LoginRequest(socialType = "kakao", socialId = socialId).toRequestBody()).data?.memberId ?: throw NoDataException()
    }.onFailure { error ->
        Timber.e("error : $error")
    }

    override fun hasToken(): Flow<Boolean> = dataStoreDataSource.hasToken()

    override suspend fun postSign(id: String, nickname: String, region: String): Result<Int> = createResult {
        loginService.postSignUp(
            SignRequest(
                socialType = "kakao",
                socialId = id,
                nickname = nickname,
                region = region.toRegion().region
            ).toRequestBody()
        ).data ?: throw NoDataException()
    }.onFailure { error ->
        Timber.e("error : $error")
    }
}
