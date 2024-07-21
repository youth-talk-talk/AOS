package com.core.exception

class InvalidValueException(
    private val m: String?
): Exception() {
    override val message: String?
        get() = m
}
