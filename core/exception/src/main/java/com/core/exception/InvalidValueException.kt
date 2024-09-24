package com.core.exception

class InvalidValueException(
    private val m: String? = "InvalidValueException"
): Exception() {
    override val message: String?
        get() = m
}
