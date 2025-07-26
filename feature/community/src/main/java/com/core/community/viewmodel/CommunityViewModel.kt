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
import com.core.exception.BadRequestException
import com.youthtalk.model.post.PostSubject
import com.youthtalk.model.post.PostType
import com.youthtalk.model.typeenum.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
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
                .onSuccess {
                    setState {
                        copy(
                            popularReviews = it.first,
                            popularFrees = it.second
                        )
                    }
                }
        }
    }

    private fun changeCategory(category: Category) {
        viewModelScope.launch {
            val posts = getPostsUseCase(category, PostType.COMMUNITY_TAB_REVIEW, PostSubject.REVIEW)
                .onStart { setState { copy(category = category) } }
                .catch {
                    Timber.e("error $it")
                }

            setState {
                copy(reviews = posts.cachedIn(viewModelScope))
            }
        }
    }

    private fun initData() {
        val currentCategory = state.value.category
        viewModelScope.launch {
            try {
                val popularReviewPostsAsync = async { getPopularPostsUseCase(category = currentCategory, PostSubject.REVIEW) }
                val popularPostsAsync = async { getPopularPostsUseCase(currentCategory, PostSubject.POST) }

                val reviewPosts = getPostsUseCase(currentCategory, PostType.COMMUNITY_TAB_REVIEW, PostSubject.REVIEW)
                val posts = getPostsUseCase(currentCategory, PostType.COMMUNITY_TAB_FREE, PostSubject.POST)

                val popularReviewPosts = popularReviewPostsAsync.await()
                val popularPosts = popularPostsAsync.await()

                setState {
                    CommunityUiState.initState.copy(
                        reviews = reviewPosts.cachedIn(viewModelScope),
                        popularReviews = popularReviewPosts.getOrThrow(),
                        frees = posts.cachedIn(viewModelScope),
                        popularFrees = popularPosts.getOrThrow()
                    )
                }
            } catch (e: BadRequestException) {
                Timber.e("error : $e")
            }
        }
    }
}
