package com.core.navigation.navigator

import com.youthtalk.model.post.PostSubject
import com.youthtalk.model.typeenum.Category
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface HomeTabNavigation {
    @Serializable
    @SerialName("Home")
    data object Home : HomeTabNavigation

    @Serializable
    @SerialName("Setting")
    data object Setting : HomeTabNavigation

    @Serializable
    @SerialName("Community")
    data class Community(val postType: PostSubject = PostSubject.REVIEW) : HomeTabNavigation

    @Serializable
    @SerialName("Policy")
    data object Policy : HomeTabNavigation
}

@Serializable
@SerialName("Account")
data object Account

@Serializable
@SerialName("PolicyOverView")
data class PolicyOverView(
    val category: Category
)
