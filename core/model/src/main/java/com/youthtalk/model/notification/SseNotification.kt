package com.youthtalk.model.notification

import kotlinx.serialization.Serializable

@Serializable
data class SseNotification(
    val notificationId: Long,
    val detail: String,
    val sender: String?,
    val postId: Long?,
    val policyId: Long?,
    val title: String,
    val message: String,
    val createdAt: String,
    val isCheck: Boolean,
    val isRecent: Boolean
)
