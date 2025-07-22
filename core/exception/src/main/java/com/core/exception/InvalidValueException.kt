package com.core.exception

class InvalidValueException(
    private val m: String? = "InvalidValueException"
): RuntimeException()  {
    override val message: String?
        get() = m
}
