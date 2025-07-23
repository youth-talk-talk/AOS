package com.core.domain.usercase.user

import com.core.dataapi.repository.LoginRepository
import javax.inject.Inject

class PostSignUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    suspend operator fun invoke(id: String, nickname: String, region: String): Result<Long> {
        val signResult = loginRepository.postSign(id, nickname, region)
        if (signResult.isFailure) return Result.failure(signResult.exceptionOrNull()!!)

        return loginRepository.postLogin(id)
    }
}
