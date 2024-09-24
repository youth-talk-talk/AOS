package com.core.exception

class NotPermissionMethod(
    private val m: String? = "NotPermissionMethod"
): Exception() {
    override val message: String?
    get() = m
}
