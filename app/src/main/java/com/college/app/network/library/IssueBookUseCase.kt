package com.college.app.network.library

import com.college.app.data.repositories.library.LibraryBooksRepository
import com.college.app.models.network.requests.IssueBookRequest
import com.college.base.AppCoroutineDispatcher
import com.college.base.domain.SuspendUseCase
import javax.inject.Inject

class IssueBookUseCase @Inject constructor(
    appCoroutineDispatcher: AppCoroutineDispatcher,
    private val libraryBooksRepository: LibraryBooksRepository,
) : SuspendUseCase<IssueBookRequest, Any>(appCoroutineDispatcher.io) {
    override suspend fun execute(parameters: IssueBookRequest): Any {
        return libraryBooksRepository.issueBookRequest(parameters)
    }
}