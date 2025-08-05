package com.core.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.usercase.GetUserUseCase
import com.core.domain.usercase.user.PostLoginUseCase
import com.core.domain.usercase.user.PostSignUseCase
import com.core.model.login.LoginUiEffect
import com.youthtalk.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val postLoginUseCase: PostLoginUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val postSignUseCase: PostSignUseCase
) : ViewModel() {
    private var socialId = ""
    private val _user = MutableSharedFlow<User?>()
    val user = _user.asSharedFlow()

    private val _error = MutableSharedFlow<Throwable>()
    val error = _error.asSharedFlow()

    var uiEffect = MutableSharedFlow<LoginUiEffect>()
        private set

    var loading = MutableStateFlow(false)

    init {
        checkToken()
    }

    private fun checkToken() {
        viewModelScope.launch {
            getUserUseCase()
                .onSuccess {
                    uiEffect.emit(LoginUiEffect.GoMainActivity)
                }.onFailure {
                    Timber.e("checkToken not User")
                    delay(500L)
                    uiEffect.emit(LoginUiEffect.GoLoginActivity)
                }
        }
    }

    fun postLogin(userId: Long) {
        socialId = "$userId"
        viewModelScope.launch {
            postLoginUseCase(socialId)
                .onSuccess {
                    uiEffect.emit(LoginUiEffect.GoMainActivity)
                }.onFailure {
                    _error.emit(it)
                }
        }
    }

    fun postSign(nickname: String, region: String) {
        viewModelScope.launch {
            loading.value = true
            postSignUseCase(socialId, nickname, region)
                .onSuccess {
                    loading.value = false
                    uiEffect.emit(LoginUiEffect.GoMainActivity)
                }.onFailure {
                    loading.value = false
                    _error.emit(it)
                }
        }
    }
}
