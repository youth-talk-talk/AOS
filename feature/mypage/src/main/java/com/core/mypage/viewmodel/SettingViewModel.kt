package com.core.mypage.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.core.base.BaseViewModel
import com.core.domain.usercase.GetImageListUseCase
import com.core.domain.usercase.GetUserUseCase
import com.core.domain.usercase.user.PostUserLogoutUseCase
import com.core.domain.usercase.user.PostUserUseCase
import com.core.mypage.model.setting.SettingType
import com.core.mypage.model.setting.SettingUiEffect
import com.core.mypage.model.setting.SettingUiEvent
import com.core.mypage.model.setting.SettingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getImageListUseCase: GetImageListUseCase,
    private val postUserUseCase: PostUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val postUserLogoutUseCase: PostUserLogoutUseCase
) : BaseViewModel<SettingUiState, SettingUiEvent, SettingUiEffect>(
    initialState = SettingUiState.initState
) {
    val permissions = mutableStateListOf<String>()

    init {
        setEvent(SettingUiEvent.InitData)
    }

    override fun handleEvents(event: SettingUiEvent) {
        when (event) {
            is SettingUiEvent.InitData -> initData()
            is SettingUiEvent.ChangeSettingType -> {
                when (event.settingType) {
                    SettingType.MAIN -> setState { copy(settingInfoType = event.settingType) }
                    SettingType.ACCOUNT -> setState { copy(accountUser = user, settingInfoType = event.settingType) }
                    SettingType.IMAGE -> {
                        setState { copy(settingInfoType = event.settingType) }
                        getImages()
                    }
                }
            }

            is SettingUiEvent.PostLogout -> postLogout(event.deleteUser)
            is SettingUiEvent.SelectImageUrl -> setState {
                copy(
                    accountUser = accountUser.copy(profileImgUrl = event.imageUrl),
                    settingInfoType = SettingType.ACCOUNT
                )
            }

            is SettingUiEvent.OnChangeValue -> setState { copy(accountUser = accountUser.copy(nickname = event.nickname)) }
            is SettingUiEvent.OnChangeRegion -> setState { copy(accountUser = accountUser.copy(region = event.region)) }
            is SettingUiEvent.OnSaveUser -> saveUser()
        }
    }

    private fun saveUser() {
        if (state.value.user == state.value.accountUser) {
            setState { copy(settingInfoType = SettingType.MAIN) }
        } else {
            viewModelScope.launch {
                postUserUseCase(state.value.accountUser.nickname, state.value.accountUser.region)
                    .onSuccess { user ->
                        Timber.i("SettingViewModel saveUser success $user")
                        setState {
                            copy(
                                user = user,
                                settingInfoType = SettingType.MAIN
                            )
                        }
                    }
            }
        }
    }

    private fun getImages() {
        viewModelScope.launch {
            getImageListUseCase()
                .onSuccess {
                    Timber.i("success $it")
                    setState { copy(images = it) }
                }
        }
    }

    private fun initData() {
        viewModelScope.launch {
            getUserUseCase()
                .onSuccess {
                    setState { copy(user = it) }
                }
        }
    }

    private fun postLogout(deleteUser: Boolean) {
        viewModelScope.launch {
            postUserLogoutUseCase(deleteUser)
                .catch {
                    Timber.e("SettingViewModel postLogout error " + it.message)
                }
                .collectLatest {
                    setEffect { SettingUiEffect.Logout }
                }
        }
    }

    fun dismissDialog() {
        permissions.removeAll(permissions)
    }

    fun onPermissionResult(permission: String, isGranted: Boolean) {
        if (!isGranted && !permissions.contains(permission)) {
            permissions.add(permission)
        }
    }
}
