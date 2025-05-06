package com.core.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface CommunityWriteNavigation {

    @Serializable
    @SerialName("Write")
    data object Write : CommunityWriteNavigation

    @Serializable
    @SerialName("PolicySearch")
    data object PolicySearch : CommunityWriteNavigation

    @Serializable
    @SerialName("Picture")
    data object Picture : CommunityWriteNavigation
}
