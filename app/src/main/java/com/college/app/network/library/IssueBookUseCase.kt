package com.college.app.network.library

import com.college.app.data.repositories.library.LibraryBooksRepository
import com.college.app.models.network.requests.IssueBookRequest
import com.college.app.models.network.responses.IssueBookResponse
import com.college.app.models.network.responses.UserLibraryResponse
import com.college.base.AppCoroutineDispatcher
import com.college.base.domain.SuspendUseCase
import javax.inject.Inject

class IssueBookUseCase @Inject constructor(
    appCoroutineDispatcher: AppCoroutineDispatcher,
    private val libraryBooksRepository: LibraryBooksRepository,
) : SuspendUseCase<IssueBookRequest, UserLibraryResponse>(appCoroutineDispatcher.io) {
    override suspend fun execute(parameters: IssueBookRequest): UserLibraryResponse {
        return libraryBooksRepository.issueBookRequest(parameters)
    }
}