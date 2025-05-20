package com.core.mypage.viewmodel

import com.core.base.BaseViewModel
import com.core.mypage.model.scrappost.ScrapPostUiEffect
import com.core.mypage.model.scrappost.ScrapPostUiEvent
import com.core.mypage.model.scrappost.ScrapPostUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScrapPostViewModel @Inject constructor() : BaseViewModel<ScrapPostUiState, ScrapPostUiEvent, ScrapPostUiEffect>(
    initialState = ScrapPostUiState.initState
) {

    override fun handleEvents(event: ScrapPostUiEvent) {
        TODO("Not yet implemented")
    }
}
