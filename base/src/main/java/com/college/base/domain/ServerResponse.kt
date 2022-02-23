package com.college.base.domain

import com.squareup.moshi.*
import java.lang.reflect.ParameterizedType

data class ServerResponse<T>(
    @Json(name = "data")
    var data: T,

    @Json(name = "message")
    var message: String = "",

    @Json(name = "status")
    var status: Int,

    @Json(name = "errCode")
    var error: Any?,

    @Json(name = "disabled")
    var disabled: Boolean,

    @Json(name = "update")
    var update: Boolean
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

inline fun <reified T> getMoshiAdapterServerResponse(): JsonAdapter<out ParameterizedType>? {
    val type = Types.newParameterizedType(ServerResponse::class.java, T::class.java)
    return Moshi.Builder().build().adapter(type::class.java)
}