package com.core.domain.exception

//401 에러 Exception
class UnauthorizedException(
    private val errorMessage : String
) : Exception() {
    override val message: String?
        get() = errorMessage
}
