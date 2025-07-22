package com.core.exception

class NetworkErrorException(
    private val m: String? = "NetworkErrorException"
) : RuntimeException() {
    override val message: String?
        get() = m
}
