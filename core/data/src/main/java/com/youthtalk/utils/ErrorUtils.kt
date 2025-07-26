package com.youthtalk.utils

import com.core.exception.BadRequestException
import com.core.exception.ConflictException
import com.core.exception.InvalidValueException
import com.core.exception.NetworkErrorException
import com.core.exception.NotFoundResource
import com.core.exception.NotPermissionMethod
import com.core.exception.UnAuthorizedException
import com.youthtalk.dto.CommonResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import retrofit2.HttpException
import timber.log.Timber

object ErrorUtils {

    @Suppress("TooGenericExceptionCaught")
    inline fun <reified T, R> T.createResult(call: T.() -> R): Result<R> {
        return try {
            Result.success(call())
        } catch (e: HttpException) {
            Timber.e(e.message)
            Result.failure(mapToCustomException<T>(e))
        } catch (e: Exception) {
            Timber.e(e.message)
            Result.failure(NetworkErrorException(e.message))
        }
    }

    inline fun <reified T> mapToCustomException(it: HttpException): Exception {
        val error = it.response()?.errorBody()?.string() ?: throw InvalidValueException(it.message)
        val response = Json.decodeFromString<CommonResponse<JsonElement?>>(error)
        return when (it.code()) {
            401 -> UnAuthorizedException(response.message)
            404 -> NotFoundResource(response.message)
            405 -> NotPermissionMethod(response.message)
            409 -> ConflictException(response.message)
            400 -> BadRequestException(response.message)
            500 -> InvalidValueException(response.message)
            else -> IllegalStateException(response.message)
        }
    }
}
