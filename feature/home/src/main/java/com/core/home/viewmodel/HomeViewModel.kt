package com.core.home.viewmodel

import androidx.lifecycle.viewModelScope
import com.core.base.BaseViewModel
import com.core.domain.usercase.ChangeCategoriesUseCase
import com.core.domain.usercase.GetCategoriesUseCase
import com.core.domain.usercase.GetUserUseCase
import com.core.domain.usercase.PostPolicyScrapUseCase
import com.core.domain.usercase.home.GetHomeDataUseCase
import com.core.home.model.HomeUiEffect
import com.core.home.model.HomeUiEvent
import com.core.home.model.HomeUiState
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
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val changeCategoriesUseCase: ChangeCategoriesUseCase,
    private val postPolicyScrapUseCase: PostPolicyScrapUseCase,
    private val getHomeDataUseCase: GetHomeDataUseCase,
    private val getUserUseCase: GetUserUseCase
) : BaseViewModel<HomeUiState, HomeUiEvent, HomeUiEffect>(
    initialState = HomeUiState()
) {

    init {
        setEvent(HomeUiEvent.GetHomeData)
    }

    override fun handleEvents(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.GetHomeData -> getHomeData()
        }
    }

    private fun getHomeData() {
        viewModelScope.launch {
            combine(
                getUserUseCase(),
                getHomeDataUseCase()
            ) { user, homeData ->
                HomeUiState(
                    isLoading = false,
                    user = user,
                    homeData = homeData
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
