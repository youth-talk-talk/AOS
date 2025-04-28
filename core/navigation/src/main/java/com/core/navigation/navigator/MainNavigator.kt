package com.core.navigation.navigator

import com.core.navigation.model.ScrapPostType
import kotlinx.serialization.Serializable

interface MainNavigator : Navigator

@Serializable
data object Etc

@Serializable
data object Terms

@Serializable
data object ScrapPolicy

@Serializable
data class ScrapPost(
    val type: ScrapPostType,
)
