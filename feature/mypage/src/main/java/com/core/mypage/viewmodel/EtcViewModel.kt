package com.core.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.usercase.user.PostUserLogoutUseCase
import com.core.mypage.model.etc.EtcUiEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class EtcViewModel @Inject constructor(
    private val postUserLogoutUseCase: PostUserLogoutUseCase
) : ViewModel() {
    private val _uiEffect = MutableSharedFlow<EtcUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    fun postLogout(deleteUser: Boolean) {
        viewModelScope.launch {
            postUserLogoutUseCase(deleteUser)
                .catch {
                    Timber.e("EtcViewModel postLogout error " + it.message)
                }
                .collectLatest {
                    _uiEffect.emit(EtcUiEffect.Logout)
                }
        }
    }
}
