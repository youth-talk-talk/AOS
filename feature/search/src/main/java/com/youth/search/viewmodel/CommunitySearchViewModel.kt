package com.youth.search.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.core.base.BaseViewModel
import com.core.domain.usercase.search.GetKeywordPostCountUseCase
import com.core.domain.usercase.search.GetKeywordPostUseCase
import com.core.domain.usercase.search.GetRecentListUseCase
import com.core.domain.usercase.search.PostRecentListUseCase
import com.youth.search.model.SearchState
import com.youth.search.model.communitysearch.CommunityUiEffect
import com.youth.search.model.communitysearch.CommunityUiEvent
import com.youth.search.model.communitysearch.CommunityUiState
import com.youthtalk.model.post.PostSubject
import com.youthtalk.model.post.PostType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class CommunitySearchViewModel @Inject constructor(
    val getRecentListUseCase: GetRecentListUseCase,
    val postRecentListUseCase: PostRecentListUseCase,
    val getKeywordPostCountUseCase: GetKeywordPostCountUseCase,
    val getKeywordPostUseCase: GetKeywordPostUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<CommunityUiState, CommunityUiEvent, CommunityUiEffect>(
    initialState = CommunityUiState.initState
) {
    init {
        val communityType = savedStateHandle.get<PostSubject>("communityType") ?: PostSubject.REVIEW
        setEvent(CommunityUiEvent.InitData(communityType))
    }

    override fun handleEvents(event: CommunityUiEvent) {
        when (event) {
            is CommunityUiEvent.InitData -> initData(event.communityType)
            is CommunityUiEvent.SetState -> setSearchState(event.state)
            is CommunityUiEvent.Search -> search(event.keyword, event.communityType)
            is CommunityUiEvent.SetRecently -> setRecentlyListUseCase(event.list)
        }
    }

    private fun search(search: String, communityType: PostSubject) {
        viewModelScope.launch {
            val list = state.value.recently.toMutableList()
            if (list.contains(search)) {
                list.remove(search)
            }
            list.add(0, search)
            setRecentlyListUseCase(list)

            combine(
                getKeywordPostUseCase(search, postSubject = communityType, postType = PostType.SEARCH),
                getKeywordPostCountUseCase(keyword = search, communityType = communityType)
            ) { posts, count ->
                Pair(posts, count)
            }
                .onStart {
                    setState { copy(searchState = SearchState.SEARCH) }
                }
                .catch {
                    Timber.e("CommunitySearchViewModel search error $it")
                }
                .collectLatest { (posts, count) ->
                    setState {
                        copy(
                            searchPost = posts.cachedIn(viewModelScope),
                            totalCount = count
                        )
                    }
                }
        }
    }

    private fun setSearchState(searchState: SearchState) {
        viewModelScope.launch {
            setState { copy(searchState = searchState) }
        }
    }

    private fun setRecentlyListUseCase(recently: List<String>) {
        Timber.e("setRecentlyListUseCase recently $recently")
        viewModelScope.launch {
            postRecentListUseCase(recently)
        }
    }

    private fun initData(communityType: PostSubject) {
        viewModelScope.launch {
            getRecentListUseCase()
                .catch {
                    Timber.e("PolicySearchViewModel initData error $it")
                }
                .collectLatest {
                    Timber.e("PolicySearchViewModel initData success $it")
                    setState { copy(communityType = communityType, recently = it) }
                }
        }
    }
}
