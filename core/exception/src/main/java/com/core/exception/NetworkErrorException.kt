package com.core.exception

class NetworkErrorException(
    private val m: String?
) : Exception() {
    override val message: String?
        get() = m
}
