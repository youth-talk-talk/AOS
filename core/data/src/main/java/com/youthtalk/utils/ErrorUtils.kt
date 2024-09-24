package com.youthtalk.utils

import com.core.exception.InvalidValueException
import com.core.exception.NetworkErrorException
import com.core.exception.NotFoundResource
import com.core.exception.NotPermissionMethod
import com.core.exception.UnAuthorizedException
import com.youthtalk.dto.CommonResponse
import kotlinx.serialization.json.Json
import retrofit2.HttpException

object ErrorUtils {
    inline fun <reified T> throwableError(it: Throwable) {
        when (it) {
            is HttpException -> {
                val error = it.response()?.errorBody()?.string() ?: throw InvalidValueException(it.message)
                val response = Json.decodeFromString<CommonResponse<T>>(error)
                when (it.code()) {
                    401 -> throw UnAuthorizedException(response.message)
                    404 -> throw NotFoundResource(response.message)
                    405 -> throw NotPermissionMethod(response.message)
                    500 -> throw InvalidValueException(response.message)
                }
            }

            else -> {
                throw NetworkErrorException()
            }
        }
    }
}
