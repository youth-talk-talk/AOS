package com.core.mypage.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.core.base.BaseViewModel
import com.core.domain.usercase.post.GetSettingPostCountUseCase
import com.core.domain.usercase.post.GetSettingPostUseCase
import com.core.domain.usercase.post.PostPostScrapUseCase
import com.core.mypage.model.scrappost.ScrapPostUiEffect
import com.core.mypage.model.scrappost.ScrapPostUiEvent
import com.core.mypage.model.scrappost.ScrapPostUiState
import com.core.navigation.model.ScrapPostType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class ScrapPostViewModel @Inject constructor(
    private val getSettingPostUseCase: GetSettingPostUseCase,
    private val getSettingPostCountUseCase: GetSettingPostCountUseCase,
    private val postPostScrapUseCase: PostPostScrapUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ScrapPostUiState, ScrapPostUiEvent, ScrapPostUiEffect>(
    initialState = ScrapPostUiState.initState
) {

    init {
        savedStateHandle.get<ScrapPostType>("type")?.let {
            setEvent(ScrapPostUiEvent.InitData(it == ScrapPostType.SCRAP, it))
        }
    }

    override fun handleEvents(event: ScrapPostUiEvent) {
        when (event) {
            is ScrapPostUiEvent.InitData -> initData(event.isScrap, event.type)
            is ScrapPostUiEvent.RefreshCount -> refreshCount()
            is ScrapPostUiEvent.PostScrapPost -> postScrapPost(event.postId, event.scrap)
        }
    }

    private fun postScrapPost(postId: Long, scrap: Boolean) {
        viewModelScope.launch {
            postPostScrapUseCase(postId, scrap)
                .catch {
                    Timber.e("ScrapPostViewModel postScrapPost error $it")
                }
                .collectLatest {
                    val newCount = if (state.value.type == ScrapPostType.SCRAP) {
                        state.value.count - 1
                    } else {
                        state.value.count
                    }
                    setState {
                        copy(count = newCount)
                    }
                }
        }
    }

    private fun refreshCount() {
        viewModelScope.launch {
            getSettingPostCountUseCase(state.value.type == ScrapPostType.SCRAP)
                .catch {
                    Timber.e("ScrapPostViewModel refreshCount error $it")
                }
                .collectLatest { count ->
                    setState {
                        copy(count = count)
                    }
                }
        }
    }

    private fun initData(isScrap: Boolean, type: ScrapPostType) {
        viewModelScope.launch {
            combine(
                getSettingPostUseCase(isScrap),
                getSettingPostCountUseCase(isScrap)
            ) { posts, count ->
                Pair(posts, count)
            }
                .catch {
                    Timber.e("ScrapPostViewModel initData error $it")
                }
                .collectLatest { (posts, count) ->
                    setState {
                        copy(
                            isLoading = false,
                            posts = posts.cachedIn(viewModelScope),
                            type = type,
                            count = count
                        )
                    }
                }
        }
    }
}
