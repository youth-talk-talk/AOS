package com.core.community.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.community.model.CommunityUiState
import com.core.domain.usercase.post.GetPopularPostsUseCase
import com.core.domain.usercase.post.GetPostsUseCase
import com.core.domain.usercase.review.GetReviewCategoriesUseCase
import com.core.domain.usercase.review.PostPopularReviewPostsUseCase
import com.core.domain.usercase.review.PostReviewPostsUseCase
import com.core.domain.usercase.review.SetReviewCategoriesUseCase
import com.youthtalk.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val getReviewCategoriesUseCase: GetReviewCategoriesUseCase,
    private val setReviewCategoriesUseCase: SetReviewCategoriesUseCase,
    private val postReviewPostsUseCase: PostReviewPostsUseCase,
    private val postPopularReviewPostsUseCase: PostPopularReviewPostsUseCase,
    private val getPopularPostsUseCase: GetPopularPostsUseCase,
    private val getPostsUseCase: GetPostsUseCase,
) : ViewModel() {

    private val _error = MutableSharedFlow<Throwable>()
    val error = _error.asSharedFlow()

    val uiState = combine(
        getReviewCategoriesUseCase(),
        postPopularReviewPostsUseCase(),
        postReviewPostsUseCase(),
        getPopularPostsUseCase(),
        getPostsUseCase(),
    ) {
            categories, popularReviewPosts, reviewPosts, populorPosts, posts ->
        CommunityUiState.Success(
            categories = categories.toPersistentList(),
            popularReviewPosts = popularReviewPosts.toPersistentList(),
            reviewPosts = reviewPosts,
            popularPosts = populorPosts.toPersistentList(),
            posts = posts,
        )
    }
        .catch {
            Log.d("YOON-CHAN", "CommunityViewModel getReviewCategoriesUseCase error")
        }
        .stateIn(
            initialValue = CommunityUiState.Loading,
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
        )

    fun setCategories(category: Category?) {
        category?.let {
            val state = uiState.value
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
            }
        }
    }
}
