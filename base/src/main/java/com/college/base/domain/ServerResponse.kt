package com.college.base.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ServerResponse<T>(
    @Json(name = "data")
    var data: T,

    @Json(name = "message")
    var message: String = "",

    @Json(name = "status")
    var status: Int,
)

fun <T> ServerResponse<T>.dataOrThrowException(executeOnSuccess: T.() -> Unit = {}): T {
    return if (isSuccess()) data.also(executeOnSuccess) else throw serverException()
}

suspend fun <T> ServerResponse<T>.suspendDataOrThrowException(
    executeOnSuccess: suspend T.() -> Unit = {}
): T {
    return if (isSuccess()) data.also { executeOnSuccess(it) }
    else throw serverException()
}

suspend fun <T, E> ServerResponse<T>.mapDataOrThrowException(
    mapper: suspend (T) -> E, executeOnSuccess: suspend T.() -> Unit = {}
): E {
    return if (isSuccess()) {
        executeOnSuccess(data)
        mapper(data)
    } else throw serverException()
}

fun <T> ServerResponse<T>.isSuccess() = status == 200

fun <T> ServerResponse<T>.serverException() = ServerException(status, message)