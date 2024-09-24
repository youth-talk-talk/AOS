package com.core.exception

class NetworkErrorException(
    private val m: String? = "NetworkErrorException"
) : Exception() {
    override val message: String?
        get() = m
}
