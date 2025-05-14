package com.core.community.viewmodel

import com.core.base.BaseViewModel
import com.core.community.model.community.CommunityUiEffect
import com.core.community.model.community.CommunityUiEvent
import com.core.community.model.community.CommunityUiState
import com.core.domain.usercase.post.GetPopularPostsUseCase
import com.core.domain.usercase.post.GetPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import timber.log.Timber

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
    private val getPopularPostsUseCase: GetPopularPostsUseCase
) : BaseViewModel<CommunityUiState, CommunityUiEvent, CommunityUiEffect>(
    initialState = CommunityUiState.initState
) {

    override fun handleEvents(event: CommunityUiEvent) {
        Timber.e("handleEvents")
    }
}
