package com.core.navigation.navigator

import kotlinx.serialization.Serializable

sealed interface Navigation {
    @Serializable
    data object Main : Navigation
}
