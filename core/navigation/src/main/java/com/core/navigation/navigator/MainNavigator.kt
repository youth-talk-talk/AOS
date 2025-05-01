package com.core.navigation.navigator

import com.core.navigation.model.CommentType
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

@Serializable
data class Comment(
    val type: CommentType,
)

@Serializable
data object Notification

@Serializable
data object PolicySearch

@Serializable
data class PolicySearchResult(
    val search: String,
)

@Serializable
data object PopularPolicy
