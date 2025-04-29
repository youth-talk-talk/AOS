package com.core.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.usercase.mypage.PostUserLogoutUseCase
import com.core.mypage.model.account.AccountUiEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val postUserLogoutUseCase: PostUserLogoutUseCase,
) : ViewModel() {

    private val _uiEffect = MutableSharedFlow<AccountUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    fun postLogout(deleteUser: Boolean) {
        viewModelScope.launch {
            postUserLogoutUseCase(deleteUser)
                .catch {
                    Timber.e("AccountViewModel postLogout error " + it.message)
                }
                .collectLatest {
                    _uiEffect.emit(AccountUiEffect.Logout)
                }
        }
    }
}
