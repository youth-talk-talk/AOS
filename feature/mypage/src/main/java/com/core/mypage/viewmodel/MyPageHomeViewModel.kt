package com.core.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.domain.usercase.GetUserUseCase
import com.core.domain.usercase.mypage.GetDeadlinePolicesUseCase
import com.core.domain.usercase.mypage.PostUserLogoutUseCase
import com.core.domain.usercase.mypage.PostUserUseCase
import com.core.mypage.model.home.MyPageHomeUiEffect
import com.core.mypage.model.home.MyPageHomeUiEvent
import com.core.mypage.model.home.MyPageHomeUiState
import com.youthtalk.model.Region
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyPageHomeViewModel @Inject constructor(
    private val getDeadlinePolicesUseCase: GetDeadlinePolicesUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val postUserUseCase: PostUserUseCase,
    private val postUserLogoutUseCase: PostUserLogoutUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<MyPageHomeUiState>(MyPageHomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<MyPageHomeUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    init {
        viewModelScope.launch {
            combine(
                getDeadlinePolicesUseCase(),
                getUserUseCase(),
            ) { deadlinePolicies, user ->
                MyPageHomeUiState.Success(
                    deadlinePolicies = deadlinePolicies.toPersistentList(),
                    user = user,
                )
            }
                .catch {
                    Timber.e("MyPageHomeViewModel error " + it.message)
                }
                .collectLatest {
                    _uiState.value = it
                }
        }
    }

    fun uiEvent(event: MyPageHomeUiEvent) {
        when (event) {
            is MyPageHomeUiEvent.ChangeNickname -> changeNickname(nickname = event.nickname)
            is MyPageHomeUiEvent.ChangeRegion -> changeNickname(region = event.region)
            is MyPageHomeUiEvent.Logout -> postLogout(event.deleteUser)
        }
    }

    private fun changeNickname(nickname: String? = null, region: Region? = null) {
        val state = _uiState.value
        if (state !is MyPageHomeUiState.Success) return
        val changeName = nickname ?: state.user.nickname
        val changeRegion = region ?: state.user.region
        viewModelScope.launch {
            postUserUseCase(changeName, changeRegion)
                .catch {
                    Timber.e("MyPageHomeViewModel chageNickname error " + it.message)
                }
                .collectLatest {
                    _uiState.value = state.copy(user = it)
                    nickname?.let { _uiEffect.emit(MyPageHomeUiEffect.GoBack) }
                    region?.let { _uiEffect.emit(MyPageHomeUiEffect.CloseRegionBottomSheet) }
                }
        }
    }

    private fun postLogout(deleteUser: Boolean) {
        viewModelScope.launch {
            postUserLogoutUseCase(deleteUser)
                .catch {
                    Timber.e("MyPageHomeViewModel postLogout error " + it.message)
                }
                .collectLatest {
                    _uiEffect.emit(MyPageHomeUiEffect.GoLogin)
                }
        }
    }
}
