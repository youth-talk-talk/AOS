package com.core.domain.usercase.user

import com.core.dataapi.repository.LoginRepository
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapMerge

class PostSignUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(id: String, nickname: String, region: String) = loginRepository.postSign(id, nickname, region)
        .flatMapMerge { loginRepository.postLogin(id) }
}
