package com.youthtalk.model

import kotlinx.serialization.Serializable

@Serializable
data class SignErrorResponse(
    val messages: List<String>,
)
