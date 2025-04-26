package com.core.navigation.navigator

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
    data object Community : HomeTabNavigation

    @Serializable
    @SerialName("Policy")
    data object Policy : HomeTabNavigation
}

@Serializable
data object Account
