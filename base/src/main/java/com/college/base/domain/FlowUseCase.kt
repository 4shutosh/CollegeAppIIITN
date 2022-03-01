package com.college.base.domain

import com.college.base.result.DataUiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {

    operator fun invoke(parameters: P): Flow<DataUiResult<R>> {

        return execute(parameters)
            .catch { e -> emit(DataUiResult.Error(Exception(e))) }
            .flowOn(coroutineDispatcher)
    }

    abstract fun execute(parameters: P): Flow<DataUiResult<R>>
}
