package com.core.login

import android.util.Log
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
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val postLoginUseCase: PostLoginUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val postSignUseCase: PostSignUseCase,
) : ViewModel() {

    var kakaoId = ""

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
                    Log.d("YOON-CHAN", "checkToken error $it")
                    _user.emit(null)
                }
                .collectLatest {
                    Log.d("YOON-CHAN", "checkToken success $it")
                    _user.emit(it)
                }
        }
    }

    fun postLogin(userId: Long) {
        kakaoId = "kakao$userId"
        viewModelScope.launch {
            postLoginUseCase("kakao$userId")
                .catch {
                    _error.emit(it)
                }
                .collectLatest {
                    Log.d("YOON-CHAN", "viewModel postLogin $it")
                    _memberId.emit(it)
                }
        }
    }

    fun postSign(nickname: String, region: String) {
        viewModelScope.launch {
            Log.d("YOON-CHAN", "postSign")
            postSignUseCase(kakaoId, nickname, region)
                .catch {
                    _error.emit(it)
                }
                .collectLatest {
                    Log.d("YOON-CHAN", "viewModel postSign $it")
                    _memberId.emit(it)
                }
        }
    }
}
