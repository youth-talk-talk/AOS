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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
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
                .catch {
                    Timber.e("checkToken not User")
                    delay(500L)
                    uiEffect.emit(LoginUiEffect.GoLoginActivity)
                }
                .collectLatest {
                    uiEffect.emit(LoginUiEffect.GoMainActivity)
                }
        }
    }

    fun postLogin(userId: Long) {
        socialId = "$userId"
        viewModelScope.launch {
            postLoginUseCase("950331")
                .catch {
                    Timber.e("viewModel postLogin error $it")
                    _error.emit(it)
                }
                .collectLatest {
                    uiEffect.emit(LoginUiEffect.GoMainActivity)
                }
        }
    }

    fun postSign(nickname: String, region: String) {
        Timber.e("postSign Start")
        viewModelScope.launch {
            postSignUseCase(socialId, nickname, region)
                .onStart {
                    loading.value = true
                }
                .onCompletion {
                    loading.value = false
                }
                .catch {
                    Timber.e("viewModel sign error $it")
                    _error.emit(it)
                }
                .collectLatest {
                    uiEffect.emit(LoginUiEffect.GoMainActivity)
                }
        }
    }
}
