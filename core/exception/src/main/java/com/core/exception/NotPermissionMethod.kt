package com.core.exception

class NotPermissionMethod(
    private val m: String? = "NotPermissionMethod"
): RuntimeException()  {
    override val message: String?
    get() = m
}
