package com.college.base.result

import com.college.base.domain.ServerException
import kotlinx.coroutines.CancellationException

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class DataUiResult<out R> {

    data class Success<out T>(val data: T) : DataUiResult<T>()
    data class Error(val exception: Exception) : DataUiResult<Nothing>()
    data class Loading(val loading: Boolean) : DataUiResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            is Loading -> "Loading[isLoading=$loading]"
        }
    }
}

/**
 * [Success.data] if [DataUiResult] is of type [Success]
 */
fun <T> DataUiResult<T>.successOr(fallback: T): T {
    return (this as? DataUiResult.Success<T>)?.data ?: fallback
}

inline fun <T> DataUiResult<T>.onServerError(task: ServerException.() -> Unit): DataUiResult<T> {
    if (this is DataUiResult.Error && exception is ServerException) {
        this.exception.task()
    }
    return this
}

inline fun <reified T> DataUiResult<T>.onSuccess(task: T.() -> Unit): DataUiResult<T> {
    if (this is DataUiResult.Success) {
        task(data)
    }
    return this
}


inline fun <T> DataUiResult<T>.onError(task: DataUiResult.Error.() -> Unit): DataUiResult<T> {
    if (this is DataUiResult.Error && (exception is ServerException).not()) {
        task()
    }
    return this
}

inline fun <T> DataUiResult<T>.onErrorWithoutCancellation(task: DataUiResult.Error.() -> Unit): DataUiResult<T> {
    if (this is DataUiResult.Error && (exception is ServerException).not() && (exception is CancellationException).not()) {
        task()
    }
    return this
}


val <T> DataUiResult<T>.data: T?
    get() = (this as? DataUiResult.Success)?.data


inline fun <reified T> DataUiResult<T>.onResult(task: () -> Unit): DataUiResult<T> {
    task()
    return this
}