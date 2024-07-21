package com.core.exception

class NoDataException(
    private val m: String?
): Exception() {
    override val message: String?
        get() = m
}
