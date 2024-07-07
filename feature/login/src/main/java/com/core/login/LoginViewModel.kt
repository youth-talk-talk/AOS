package com.core.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.usercase.CheckTokenUseCase
import com.core.domain.usercase.PostLoginUseCase
import com.youthtalk.model.Token
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val postLoginUseCase: PostLoginUseCase,
    private val checkTokenUseCase: CheckTokenUseCase,
) : ViewModel() {
    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    private val _hasToken = MutableSharedFlow<Boolean>()
    val hasToken = _hasToken.asSharedFlow()

    private val _token = MutableStateFlow<Token?>(null)
    val token = _token.asStateFlow()

    init {
        checkToken()
    }

    private fun checkToken() {
        viewModelScope.launch {
            checkTokenUseCase()
                .catch {
                    Log.d("YOON-CHAN", "checkTokenUseCase Error $it")
                }
                .collectLatest {
                    Log.d("YOON-CHAN", "checkTokenUseCase Success $it")
                    _hasToken.emit(it)
                }
        }
    }

    fun postLogin(userId: Long) {
        viewModelScope.launch {
            postLoginUseCase("kakao$userId")
                .catch {
                    _error.emit(it.message ?: "")
                }
                .collectLatest {
                    Log.d("YOON-CHAN", "LoginViewModel ${it.accessToken} ${it.refreshToken}")
                    _token.value = it
                }
        }
    }
}
