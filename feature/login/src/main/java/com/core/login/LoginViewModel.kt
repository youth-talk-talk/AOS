package com.core.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.usercase.GetUserUseCase
import com.core.domain.usercase.PostLoginUseCase
import com.core.domain.usercase.PostSignUseCase
import com.youthtalk.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val postLoginUseCase: PostLoginUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val postSignUseCase: PostSignUseCase,
) : ViewModel() {
    private var socialId = ""
    private val _user = MutableSharedFlow<User?>()
    val user = _user.asSharedFlow()

    private val _error = MutableSharedFlow<Throwable>()
    val error = _error.asSharedFlow()

    private val _memberId = MutableSharedFlow<Long>()
    val memberId = _memberId.asSharedFlow()

    init {
        checkToken()
    }

    private fun checkToken() {
        viewModelScope.launch {
            getUserUseCase()
                .catch {
                    _user.emit(null)
                }
                .collectLatest {
                    _user.emit(it)
                }
        }
    }

    fun postLogin(userId: Long) {
        socialId = "$userId"
        viewModelScope.launch {
            postLoginUseCase(socialId)
                .catch {
                    Timber.e("viewModel postLogin error $it")
                    _error.emit(it)
                }
                .collectLatest {
                    _memberId.emit(it)
                }
        }
    }

    fun postSign(nickname: String, region: String) {
        viewModelScope.launch {
            postSignUseCase(socialId, nickname, region)
                .catch {
                    Timber.e("viewModel sign error $it")
                    _error.emit(it)
                }
                .collectLatest {
                    _memberId.emit(it)
                }
        }
    }
}
