package com.core.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.core.base.BaseViewModel
import com.core.domain.usercase.GetUserUseCase
import com.core.domain.usercase.home.GetHomeDataUseCase
import com.core.domain.usercase.home.GetNewPolicesUseCase
import com.core.domain.usercase.mypage.PostUserUseCase
import com.core.home.model.HomeUiEffect
import com.core.home.model.HomeUiEvent
import com.core.home.model.HomeUiState
import com.youthtalk.model.Region
import com.youthtalk.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeDataUseCase: GetHomeDataUseCase,
    private val getNewPolicesUseCase: GetNewPolicesUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val postUserUseCase: PostUserUseCase
) : BaseViewModel<HomeUiState, HomeUiEvent, HomeUiEffect>(
    initialState = HomeUiState()
) {

    init {
        setEvent(HomeUiEvent.GetHomeData)
    }

    override fun handleEvents(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.GetHomeData -> getHomeData()
            is HomeUiEvent.PostRegion -> postUser(event.user, event.region)
        }
    }

    private fun postUser(user: User, region: Region) {
        viewModelScope.launch {
            postUserUseCase(user.nickname, region)
                .catch {
                    Timber.e("HomeViewModel postUser error $it")
                }
                .collectLatest {
                    setEvent(HomeUiEvent.GetHomeData)
                }
        }
    }

    private fun getHomeData() {
        viewModelScope.launch {
            combine(
                getUserUseCase(),
                getHomeDataUseCase(),
                getNewPolicesUseCase()
            ) { user, homeData, newPolices ->
                HomeUiState(
                    isLoading = false,
                    user = user,
                    homeData = homeData,
                    newPolicies = newPolices
                )
            }
                .onStart {
                    setState { copy(isLoading = true) }
                }
                .catch {
                    Timber.e("HomeViewModel getHomeData error $it")
                }
                .collectLatest { uiState ->
                    setState { uiState }
                }
        }
    }
}
