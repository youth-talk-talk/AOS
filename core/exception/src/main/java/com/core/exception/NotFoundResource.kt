package com.core.exception

class NotFoundResource(
    private val m: String? = "NotFoundResource"
): Exception() {
    override val message: String?
    get() = m
}
