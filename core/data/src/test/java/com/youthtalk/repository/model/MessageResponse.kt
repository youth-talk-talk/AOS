package com.youthtalk.repository.model

import kotlinx.serialization.Serializable

@Serializable
data class MessageResponse(val message: List<String>)
