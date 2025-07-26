package com.core.community.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.core.base.BaseViewModel
import com.core.community.model.community.CommunityUiEffect
import com.core.community.model.community.CommunityUiEvent
import com.core.community.model.community.CommunityUiState
import com.core.domain.usercase.post.GetPopularPostsUseCase
import com.core.domain.usercase.post.GetPostsUseCase
import com.core.domain.usercase.post.PostPostScrapUseCase
import com.core.domain.usercase.post.SyncPopularPostUseCase
import com.youthtalk.model.post.PostSubject
import com.youthtalk.model.post.PostType
import com.youthtalk.model.typeenum.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
    private val getPopularPostsUseCase: GetPopularPostsUseCase,
    private val syncPopularPostUseCase: SyncPopularPostUseCase,
    private val postPostScrapUseCase: PostPostScrapUseCase
) : BaseViewModel<CommunityUiState, CommunityUiEvent, CommunityUiEffect>(
    initialState = CommunityUiState.initState
) {
    init {
        setEvent(CommunityUiEvent.InitData)
    }

    override fun handleEvents(event: CommunityUiEvent) {
        when (event) {
            is CommunityUiEvent.InitData -> initData()
            is CommunityUiEvent.ChangeCategory -> changeCategory(event.category)
            is CommunityUiEvent.SyncPostDate -> syncPopularPost()
            is CommunityUiEvent.PostScrapPost -> postPostScrap(event.postId, event.scrap)
        }
    }

    private fun postPostScrap(postId: Long, scrap: Boolean) {
        viewModelScope.launch {
            postPostScrapUseCase(postId, scrap)
                .onSuccess {
                    setState {
                        copy(
                            popularFrees = popularFrees.map { post ->
                                if (post.postId == postId) {
                                    post.copy(
                                        scrap = !scrap,
                                        scrapCount = post.scrapCount + (if (scrap) -1 else 1)
                                    )
                                } else {
                                    post
                                }
                            },
                            popularReviews = popularReviews.map { post ->
                                if (post.postId == postId) {
                                    post.copy(
                                        scrap = !scrap,
                                        scrapCount = post.scrapCount + (if (scrap) -1 else 1)
                                    )
                                } else {
                                    post
                                }
                            }
                        )
                    }
                }
        }
    }

    private fun syncPopularPost() {
        viewModelScope.launch {
            syncPopularPostUseCase(state.value.popularReviews, state.value.popularFrees)
                .catch {
                    Timber.e("CommunityViewModel syncPopularPost error $it")
                }
                .collectLatest { (reviews, frees) ->
                    Timber.e("CommunityViewModel syncPopularPost success $reviews $frees")
                    setState {
                        copy(
                            popularReviews = reviews,
                            popularFrees = frees
                        )
                    }
                }
        }
    }

    private fun changeCategory(category: Category) {
        viewModelScope.launch {
            getPostsUseCase(category, PostType.COMMUNITY_TAB_REVIEW, PostSubject.REVIEW)
                .onStart { setState { copy(category = category) } }
                .catch {
                    Timber.e("CommunityViewModel changeCategory error $it")
                }
                .collectLatest {
                    setState { copy(reviews = it.cachedIn(viewModelScope)) }
                }
        }
    }

    private fun initData() {
        val currentCategory = state.value.category
        viewModelScope.launch {
            val popularReviewPost = getPopularPostsUseCase(category = currentCategory, PostSubject.REVIEW)
            val popularPost = getPopularPostsUseCase(currentCategory, PostSubject.POST)

            combine(
                getPostsUseCase(currentCategory, PostType.COMMUNITY_TAB_REVIEW, PostSubject.REVIEW),
                getPostsUseCase(currentCategory, PostType.COMMUNITY_TAB_FREE, PostSubject.POST)
            ) { reviewPost, freePost ->
                CommunityUiState.initState.copy(
                    reviews = reviewPost.cachedIn(viewModelScope),
                    popularReviews = popularReviewPost.getOrThrow(),
                    frees = freePost.cachedIn(viewModelScope),
                    popularFrees = popularPost.getOrThrow()
                )
            }.collectLatest {
                setState { it }
            }
        }
    }
}
