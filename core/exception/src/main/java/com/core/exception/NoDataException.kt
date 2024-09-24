package com.core.exception

class NoDataException(
    private val m: String? = "NoDataException"
): Exception() {
    override val message: String?
        get() = m
}
