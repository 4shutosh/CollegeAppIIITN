package com.college.app.network.library

import com.college.app.data.repositories.library.LibraryBooksRepository
import com.college.app.network.models.requests.IssueBookRequest
import com.college.base.AppCoroutineDispatcher
import com.college.base.domain.SuspendUseCase
import javax.inject.Inject

class GetBookUseCase @Inject constructor(
    appCoroutineDispatcher: AppCoroutineDispatcher,
    private val libraryBooksRepository: LibraryBooksRepository,
) : SuspendUseCase<Long, Any>(appCoroutineDispatcher.io) {
    override suspend fun execute(libraryBookNumber: Long): Any {
        return libraryBooksRepository.getBookByLibraryNumber(libraryBookNumber)
    }
}