package com.core.community.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.core.community.model.CommunityUiEvent
import com.core.community.model.CommunityUiState
import com.core.domain.usercase.PostPostScrapUseCase
import com.core.domain.usercase.post.GetPopularPostsUseCase
import com.core.domain.usercase.post.GetPostsUseCase
import com.core.domain.usercase.review.GetReviewCategoriesUseCase
import com.core.domain.usercase.review.PostPopularReviewPostsUseCase
import com.core.domain.usercase.review.PostReviewPostsUseCase
import com.core.domain.usercase.review.SetReviewCategoriesUseCase
import com.youthtalk.model.Category
import com.youthtalk.model.PostType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val getReviewCategoriesUseCase: GetReviewCategoriesUseCase,
    private val setReviewCategoriesUseCase: SetReviewCategoriesUseCase,
    private val postReviewPostsUseCase: PostReviewPostsUseCase,
    private val postPopularReviewPostsUseCase: PostPopularReviewPostsUseCase,
    private val getPopularPostsUseCase: GetPopularPostsUseCase,
    private val getPostsUseCase: GetPostsUseCase,
    private val postPostScrapUseCase: PostPostScrapUseCase
) : ViewModel() {

    private val _error = MutableSharedFlow<Throwable>()
    val error = _error.asSharedFlow()

    private val _uiState = MutableStateFlow<CommunityUiState>(CommunityUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getData()
    }

    fun uiEvent(event: CommunityUiEvent) {
        when (event) {
            is CommunityUiEvent.PostScrap -> postScrap(event.postId, event.scrap, event.type)
            is CommunityUiEvent.GetData -> getData()
            is CommunityUiEvent.GetPopularData -> getPopularData()
        }
    }

    private fun getPopularData() {
        val state = _uiState.value
        if (state !is CommunityUiState.Success) return

        viewModelScope.launch {
            combine(
                postPopularReviewPostsUseCase(),
                getPopularPostsUseCase()
            ) { popularReviewPosts, popularPosts ->
                state.copy(
                    popularReviewPosts = popularReviewPosts.toPersistentList(),
                    popularPosts = popularPosts.toPersistentList()
                )
            }
                .catch {
                    Timber.e("CommunityViewModel getPopularData error")
                }
                .collectLatest {
                    _uiState.value = it
                }
        }
    }

    private fun getData() {
        viewModelScope.launch {
            combine(
                getReviewCategoriesUseCase(),
                postPopularReviewPostsUseCase(),
                getPopularPostsUseCase()
            ) { categories, popularReviewPosts, popularPosts ->
                CommunityUiState.Success(
                    categories = categories.toPersistentList(),
                    popularReviewPosts = popularReviewPosts.toPersistentList(),
                    reviewPosts = postReviewPostsUseCase().cachedIn(viewModelScope),
                    posts = getPostsUseCase().cachedIn(viewModelScope),
                    popularPosts = popularPosts.toPersistentList()
                )
            }
                .catch {
                    Timber.e("CommunityViewModel getReviewCategoriesUseCase error")
                }
                .collectLatest {
                    _uiState.value = it
                }
        }
    }

    private fun postScrap(postId: Long, scrap: Boolean, type: PostType) {
        val state = _uiState.value
        if (state !is CommunityUiState.Success) return

        viewModelScope.launch {
            postPostScrapUseCase(postId, scrap, type)
                .catch {
                    Timber.e("CommunityViewModel postScrap error " + it.message)
                }
                .collectLatest {
                    Timber.e("CommunityViewModel postScrap Success $it")
                    when (type) {
                        PostType.POST -> {
                            val list = state.popularPosts.map { post ->
                                if (post.postId == postId) {
                                    post.copy(
                                        scrap = !scrap,
                                        scraps = if (!scrap) post.scraps + 1 else post.scraps - 1
                                    )
                                } else {
                                    post
                                }
                            }
                            _uiState.value = state.copy(
                                popularPosts = list.toPersistentList()
                            )
                        }

                        else -> {
                            val list = state.popularReviewPosts.map { post ->
                                if (post.postId == postId) {
                                    post.copy(
                                        scrap = !scrap,
                                        scraps = if (!scrap) post.scraps + 1 else post.scraps - 1
                                    )
                                } else {
                                    post
                                }
                            }
                            _uiState.value = state.copy(
                                popularReviewPosts = list.toPersistentList()
                            )
                        }
                    }
                }
        }
    }

    fun setCategories(category: Category?) {
        category?.let {
            val state = _uiState.value
            if (state !is CommunityUiState.Success) return

            val categories = state.categories.toMutableList()
            if (categories.contains(category) && categories.size <= 1) return

            if (categories.contains(category)) {
                categories.remove(category)
            } else {
                categories.add(category)
            }
            viewModelScope.launch {
                _uiState.value = state.copy(
                    reviewPosts = setReviewCategoriesUseCase(categories).cachedIn(viewModelScope),
                    categories = categories.toPersistentList()
                )
            }
        }
    }
}
