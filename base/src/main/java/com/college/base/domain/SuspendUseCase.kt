package com.college.base.domain

import com.college.base.logger.CollegeLogger
import com.college.base.result.DataUiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

abstract class SuspendUseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {

    @Inject
    lateinit var logger: CollegeLogger

    /** Executes the use case asynchronously and returns a [DataUiResult].
     *
     * @return a [DataUiResult].
     *
     * @param parameters the input parameters to run the use case with
     */
    suspend operator fun invoke(parameters: P): DataUiResult<R> {
        return try {
            // Moving all use case's executions to the injected dispatcher
            // In production code, this is usually the Default dispatcher (background thread)
            // In tests, this becomes a TestCoroutineDispatcher
            withContext(coroutineDispatcher) {
                execute(parameters).let {
                    DataUiResult.Success(it)
                }
            }
        } catch (e: Exception) {
            logger.e(e)
            DataUiResult.Error(e)
        }
    }

    /**
     * Override this to set the code to be executed.
     */
    @Throws(RuntimeException::class)
    protected abstract suspend fun
            execute(parameters: P): R
}
