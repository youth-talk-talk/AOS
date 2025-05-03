package com.core.navigation.navigator

import com.core.navigation.model.CommentType
import com.core.navigation.model.ScrapPostType
import com.youthtalk.model.CommunityType
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
data class CommunitySearch(
    val communityType: CommunityType,
)

@Serializable
data object PopularPolicy

@Serializable
data object NewPolicy

@Serializable
data object RecentlyViewPolicy

@Serializable
data object DeadlinePolicy

@Serializable
data object CommunityDetail

@Serializable
data object PolicyDetail
