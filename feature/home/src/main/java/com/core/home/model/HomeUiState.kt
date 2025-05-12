package com.core.home.model

import androidx.compose.runtime.Immutable
import com.core.base.model.UiState
import com.youthtalk.model.Region
import com.youthtalk.model.User
import com.youthtalk.model.home.HomeData
import com.youthtalk.model.home.NewPolicies

@Immutable
data class HomeUiState(
    val isLoading: Boolean = true,
    val user: User = User(
        memberId = 0,
        nickname = "",
        region = Region.ALL
    ),
    val homeData: HomeData = HomeData(
        popularPolicies = listOf(),
        policiesWithReviews = listOf(),
        bestPosts = listOf()
    ),
    val newPolicies: NewPolicies = NewPolicies(
        all = listOf(),
        job = listOf(),
        dwelling = listOf(),
        education = listOf(),
        life = listOf(),
        participation = listOf()
    )
) : UiState
