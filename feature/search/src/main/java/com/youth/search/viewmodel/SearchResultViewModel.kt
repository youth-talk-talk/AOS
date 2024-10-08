package com.youth.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.core.domain.usercase.PostPolicyScrapUseCase
import com.core.domain.usercase.PostPostScrapUseCase
import com.core.domain.usercase.home.GetHomePolicyMapUseCase
import com.core.domain.usercase.post.GetPostScrapUseCase
import com.core.domain.usercase.search.GetSearchPostCountUseCase
import com.core.domain.usercase.search.GetSearchPostsUseCase
import com.core.domain.usercase.specpolicy.GetPolicyCountUseCase
import com.core.domain.usercase.specpolicy.GetUserFilterInfoUseCase
import com.core.domain.usercase.specpolicy.PostFilterUseCase
import com.core.domain.usercase.specpolicy.PostSpecPoliciesUseCase
import com.youth.search.model.SearchResultUiEvent
import com.youth.search.model.SearchResultUiState
import com.youthtalk.model.FilterInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    private val getPolicyCountUseCase: GetPolicyCountUseCase,
    private val getSpecPoliciesUseCase: PostSpecPoliciesUseCase,
    private val getUserFilterInfoUseCase: GetUserFilterInfoUseCase,
    private val getPostsCountUseCase: GetSearchPostCountUseCase,
    private val getPostsUseCase: GetSearchPostsUseCase,
    private val getHomePolicyMapUseCase: GetHomePolicyMapUseCase,
    private val getPostScrapUseCase: GetPostScrapUseCase,
    private val postPolicyScrapUseCase: PostPolicyScrapUseCase,
    private val postPostScrapUseCase: PostPostScrapUseCase,
    private val postFilterUseCase: PostFilterUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<SearchResultUiState>(SearchResultUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun uiEvent(event: SearchResultUiEvent) {
        when (event) {
            is SearchResultUiEvent.GetPost -> getPosts(event.type, event.keyword)
            is SearchResultUiEvent.GetPolicies -> getPolicies(event.keyword)
            is SearchResultUiEvent.PostPolicyScrap -> postPolicyScrap(event.postId, event.scrap)
            is SearchResultUiEvent.PostFilterInfo -> postFilterInfo(event.filterInfo)
            is SearchResultUiEvent.GetFilterInfo -> getFilterInfo()
            is SearchResultUiEvent.FilterApply -> applyFilter(event.search)
            is SearchResultUiEvent.PostPostScrap -> postPostScrap(event.postId, event.scrap)
        }
    }

    private fun postPostScrap(postId: Long, scrap: Boolean) {
        val state = _uiState.value
        if (state !is SearchResultUiState.Success) return

        viewModelScope.launch {
            postPostScrapUseCase(postId, scrap)
                .catch {
                    Timber.e("SearchResultViewModel applyFilter error " + it.message)
                }
                .collectLatest {
                    _uiState.value = state.copy(
                        postScrapMap = it,
                    )
                }
        }
    }

    private fun applyFilter(search: String) {
        val state = _uiState.value
        if (state !is SearchResultUiState.Success) return

        viewModelScope.launch {
            postFilterUseCase(state.filterInfo)
                .catch {
                    Timber.e("SearchResultViewModel applyFilter error " + it.message)
                }
                .collectLatest {
                    getPolicies(search)
                }
        }
    }

    private fun getFilterInfo() {
        val state = _uiState.value
        if (state !is SearchResultUiState.Success) return

        viewModelScope.launch {
            getUserFilterInfoUseCase()
                .catch {
                    Timber.e("SearchResultViewModel getFilterInfo error " + it.message)
                }
                .collectLatest {
                    _uiState.value = state.copy(
                        filterInfo = it,
                    )
                }
        }
    }

    private fun getPolicies(keyword: String) {
        viewModelScope.launch {
            combine(
                getPolicyCountUseCase(null, keyword),
                getSpecPoliciesUseCase(null, keyword),
                getUserFilterInfoUseCase(),
                getHomePolicyMapUseCase(),
                getPostScrapUseCase(),

            ) { count, policies, filterInfo, policyMap, postMap ->
                SearchResultUiState.Success(
                    policies = policies.cachedIn(viewModelScope),
                    count = count,
                    filterInfo = filterInfo,
                    policyScrapMap = policyMap,
                    postScrapMap = postMap,
                )
            }
                .onStart {
                    _uiState.value = SearchResultUiState.Loading
                }
                .catch {
                    Timber.e("SearchResultViewModel getPolicies error " + it.message)
                }
                .collectLatest {
                    _uiState.value = it
                }
        }
    }

    private fun getPosts(type: String, keyword: String) {
        viewModelScope.launch {
            combine(
                getUserFilterInfoUseCase(),
                getPostsCountUseCase(type, keyword),
                getPostsUseCase(type, keyword),
            ) { filterInfo, count, posts ->
                SearchResultUiState.Success(
                    posts = posts.cachedIn(viewModelScope),
                    filterInfo = filterInfo,
                    count = count,
                )
            }
                .catch {
                    Timber.e("SearchResultViewModel getPosts error " + it.message)
                }
                .collectLatest {
                    _uiState.value = it
                }
        }
    }

    private fun postPolicyScrap(postId: String, scrap: Boolean) {
        val state = _uiState.value
        if (state !is SearchResultUiState.Success) return

        viewModelScope.launch {
            postPolicyScrapUseCase(postId, scrap)
                .catch {
                    Timber.e("SearchResultViewModel postPolicyScrap error " + it.message)
                }
                .collectLatest {
                    _uiState.value = state.copy(
                        policyScrapMap = it,
                    )
                }
        }
    }

    private fun postFilterInfo(filterInfo: FilterInfo) {
        val state = _uiState.value
        if (state !is SearchResultUiState.Success) return

        _uiState.value = state.copy(
            filterInfo = filterInfo,
        )
    }

    fun clearData() {
        val state = _uiState.value
        if (state !is SearchResultUiState.Success) return

        _uiState.value = state.copy(
            policyScrapMap = mapOf(),
            postScrapMap = mapOf(),
        )
    }
}
