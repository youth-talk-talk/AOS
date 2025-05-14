package com.core.navigation.navigator

import com.core.navigation.model.CommentType
import com.core.navigation.model.ScrapPostType
import com.youthtalk.model.post.PostSubject
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
    val type: ScrapPostType
)

@Serializable
data class Comment(
    val type: CommentType
)

@Serializable
data object Notification

@Serializable
data object PolicySearch

@Serializable
data class CommunitySearch(
    val communityType: PostSubject
)

@Serializable
data object PopularPolicy

@Serializable
data class NewPolicy(
    val policies: String
)

@Serializable
data object RecentlyViewPolicy

@Serializable
data object DeadlinePolicy

@Serializable
data class CommunityDetail(
    val postId: Long
)

@Serializable
data class CommunityWrite(
    val communityType: PostSubject
)

@Serializable
data class PolicyDetail(
    val policyId: Long
)
