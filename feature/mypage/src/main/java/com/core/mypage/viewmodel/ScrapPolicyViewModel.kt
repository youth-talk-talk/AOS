package com.core.mypage.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.core.base.BaseViewModel
import com.core.domain.usercase.PostPolicyScrapUseCase
import com.core.domain.usercase.policy.GetScrapPolicyUseCase
import com.core.mypage.model.scrappolicy.ScrapPolicyUiEffect
import com.core.mypage.model.scrappolicy.ScrapPolicyUiEvent
import com.core.mypage.model.scrappolicy.ScrapPolicyUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class ScrapPolicyViewModel @Inject constructor(
    private val getScrapPolicyUseCase: GetScrapPolicyUseCase,
    private val postPolicyScrapUseCase: PostPolicyScrapUseCase
) : BaseViewModel<ScrapPolicyUiState, ScrapPolicyUiEvent, ScrapPolicyUiEffect>(
    initialState = ScrapPolicyUiState.initState
) {

    init {
        setEvent(ScrapPolicyUiEvent.InitData)
    }

    override fun handleEvents(event: ScrapPolicyUiEvent) {
        when (event) {
            is ScrapPolicyUiEvent.InitData -> initData()
            is ScrapPolicyUiEvent.PostScrap -> postPolicyScrap(event.policyId, event.scrap)
            is ScrapPolicyUiEvent.OnClickPolicy -> setEffect { ScrapPolicyUiEffect.ClickPolicy(event.policyId) }
        }
    }

    private fun postPolicyScrap(policyId: Long, scrap: Boolean) {
        viewModelScope.launch {
            postPolicyScrapUseCase(policyId, scrap)
                .onSuccess {
                    Timber.i("success : $it")
                }
        }
    }

    private fun initData() {
        viewModelScope.launch {
            getScrapPolicyUseCase()
                .catch {
                    Timber.e("ScrapPolicyViewModel initData error $it")
                }
                .collectLatest {
                    setState {
                        copy(
                            isLoading = false,
                            policies = it.cachedIn(viewModelScope)
                        )
                    }
                }
        }
    }
}
