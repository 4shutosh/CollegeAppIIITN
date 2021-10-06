package com.college.base.result

import com.college.base.domain.ServerException
import kotlinx.coroutines.CancellationException

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}

/**
 * [Success.data] if [Result] is of type [Success]
 */
fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Result.Success<T>)?.data ?: fallback
}

inline fun <T> Result<T>.onServerError(task: ServerException.() -> Unit): Result<T> {
    if (this is Result.Error && exception is ServerException) {
        this.exception.task()
    }
    return this
}

inline fun <reified T> Result<T>.onSuccess(task: T.() -> Unit): Result<T> {
    if (this is Result.Success) {
        task(data)
    }
    return this
}


inline fun <T> Result<T>.onError(task: Result.Error.() -> Unit): Result<T> {
    if (this is Result.Error && (exception is ServerException).not()) {
        task()
    }
    return this
}

inline fun <T> Result<T>.onErrorWithoutCancellation(task: Result.Error.() -> Unit): Result<T> {
    if (this is Result.Error && (exception is ServerException).not() && (exception is CancellationException).not()) {
        task()
    }
    return this
}


val <T> Result<T>.data: T?
    get() = (this as? Result.Success)?.data


inline fun <reified T> Result<T>.onResult(task: () -> Unit): Result<T> {
    task()
    return this
}

fun <T> Result<T>.dataOrThrow(executeOnSuccess: T.() -> Unit = {}): T {
    return when (this) {
        is Result.Success -> data.also { executeOnSuccess(it) }
        is Result.Error -> throw exception
    }
}
