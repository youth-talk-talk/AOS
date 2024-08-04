package com.youthtalk.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignErrorResponse(
    val messages: List<String>,
)
