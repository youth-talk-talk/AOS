package com.core.community.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.core.community.model.CommunityUiEvent
import com.core.community.model.CommunityUiState
import com.core.domain.usercase.PostPostScrapUseCase
import com.core.domain.usercase.post.GetPopularPostsUseCase
import com.core.domain.usercase.post.GetPostScrapUseCase
import com.core.domain.usercase.post.GetPostsUseCase
import com.core.domain.usercase.review.GetReviewCategoriesUseCase
import com.core.domain.usercase.review.PostPopularReviewPostsUseCase
import com.core.domain.usercase.review.PostReviewPostsUseCase
import com.core.domain.usercase.review.SetReviewCategoriesUseCase
import com.youthtalk.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val getReviewCategoriesUseCase: GetReviewCategoriesUseCase,
    private val setReviewCategoriesUseCase: SetReviewCategoriesUseCase,
    private val postReviewPostsUseCase: PostReviewPostsUseCase,
    private val postPopularReviewPostsUseCase: PostPopularReviewPostsUseCase,
    private val getPopularPostsUseCase: GetPopularPostsUseCase,
    private val getPostsUseCase: GetPostsUseCase,
    private val postPostScrapUseCase: PostPostScrapUseCase,
    private val getPostScrapUseCase: GetPostScrapUseCase,
) : ViewModel() {

    private val _error = MutableSharedFlow<Throwable>()
    val error = _error.asSharedFlow()

    private val _uiState = MutableStateFlow<CommunityUiState>(CommunityUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                getReviewCategoriesUseCase(),
                postPopularReviewPostsUseCase(),
                postReviewPostsUseCase(),
                getPostScrapUseCase(),
            ) { categories, popularReviewPosts, reviewPosts, map ->
                CommunityUiState.Success(
                    categories = categories.toPersistentList(),
                    popularReviewPosts = popularReviewPosts.toPersistentList(),
                    reviewPosts = reviewPosts.cachedIn(viewModelScope),
                    postScrapMap = map,
                )
            }.flatMapMerge {
                combine(
                    getPopularPostsUseCase(),
                    getPostsUseCase(),
                ) { popularPosts, posts ->
                    it.copy(popularPosts = popularPosts.toPersistentList(), posts = posts.cachedIn(viewModelScope))
                }
            }
                .catch {
                    Log.d("YOON-CHAN", "CommunityViewModel getReviewCategoriesUseCase error")
                }
                .collectLatest {
                    _uiState.value = it
                }
        }
    }

    fun uiEvent(event: CommunityUiEvent) {
        when (event) {
            is CommunityUiEvent.PostScrap -> postScrap(event.postId, event.scrap)
        }
    }

    private fun postScrap(postId: Long, scrap: Boolean) {
        val state = _uiState.value
        if (state !is CommunityUiState.Success) return

        viewModelScope.launch {
            postPostScrapUseCase(postId, scrap)
                .catch {
                    Log.d("YOON-CHAN", "CommunityViewModel postScrap error ${it.message}")
                }
                .collectLatest {
                    _uiState.value = state.copy(
                        postScrapMap = it,
                    )
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
                setReviewCategoriesUseCase(categories)
                    .collectLatest {
                        Log.d("YOON-CHAN", "CommunityViewModel setCategories")
                        _uiState.value = state.copy(
                            reviewPosts = it.cachedIn(viewModelScope),
                            categories = categories.toPersistentList(),
                        )
                    }
            }
        }
    }
}
