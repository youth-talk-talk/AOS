package com.core.mypage.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.core.base.BaseViewModel
import com.core.domain.usercase.GetImageListUseCase
import com.core.domain.usercase.GetUserUseCase
import com.core.domain.usercase.PostUploadImageUseCase
import com.core.domain.usercase.user.PostUserLogoutUseCase
import com.core.domain.usercase.user.PostUserUseCase
import com.core.mypage.model.setting.SettingType
import com.core.mypage.model.setting.SettingUiEffect
import com.core.mypage.model.setting.SettingUiEvent
import com.core.mypage.model.setting.SettingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getImageListUseCase: GetImageListUseCase,
    private val postUserUseCase: PostUserUseCase,
    private val postUploadImageUseCase: PostUploadImageUseCase,
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
            is SettingUiEvent.PostUploadImages -> uploadImage(event.file)
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
                Timber.e("saveUser profileImage : ${state.value.accountUser.profileImgUrl}")
                postUserUseCase(state.value.accountUser.nickname, state.value.accountUser.region, state.value.accountUser.profileImgUrl)
                    .catch {
                        Timber.e("SettingViewModel saveUser error $it")
                    }
                    .collectLatest { user ->
                        Timber.e("SettingViewModel saveUser success $user")
                        setState {
                            copy(user = user, settingInfoType = SettingType.MAIN)
                        }
                    }
            }
        }
    }

    private fun uploadImage(file: File) {
        viewModelScope.launch {
            postUploadImageUseCase(file)
                .onStart {
                    setState {
                        copy(uploadLoading = true, settingInfoType = SettingType.ACCOUNT)
                    }
                }
                .catch {
                    Timber.e("SettingViewModel uploadImage error $it")
                }
                .collectLatest { image ->
                    Timber.e("SettingViewModel uploadImage success $image")
                    setState {
                        copy(
                            uploadLoading = false,
                            accountUser = accountUser.copy(profileImgUrl = image)
                        )
                    }
                }
        }
    }

    private fun getImages() {
        viewModelScope.launch {
            getImageListUseCase()
                .catch {
                    Timber.e("SettingViewModel getImages error $it")
                }
                .collectLatest {
                    Timber.e("SettingViewModel getImages success $it")
                    setState { copy(images = it) }
                }
        }
    }

    private fun initData() {
        viewModelScope.launch {
            getUserUseCase()
                .catch {
                    Timber.e("SettingViewModel initData error $it")
                }
                .collectLatest {
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
