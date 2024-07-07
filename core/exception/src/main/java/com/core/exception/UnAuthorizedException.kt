package com.core.exception

//401 에러 Exception
class UnAuthorizedException(
    private val errorMessage : String
) : Exception() {
    override val message: String?
        get() = errorMessage
}
