package com.core.domain.usercase

import com.core.dataapi.repository.LoginRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapMerge
import javax.inject.Inject

class PostSignUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(id: String, nickname: String, region: String) = loginRepository.postSign(id, nickname, region)
        .flatMapMerge { loginRepository.postLogin(id) }
}
