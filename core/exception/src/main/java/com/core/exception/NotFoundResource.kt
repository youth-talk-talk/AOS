package com.core.exception

class NotFoundResource(
    private val m: String? = "NotFoundResource"
): RuntimeException()  {
    override val message: String?
    get() = m
}
