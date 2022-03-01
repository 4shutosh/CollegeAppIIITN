package com.college.app.data.repositories.library

import com.college.app.models.local.CollegeBook
import com.college.app.network.CollegeAppService
import com.college.app.network.models.requests.IssueBookRequest
import com.college.app.network.models.responses.IssueBookResponse
import com.college.base.domain.dataOrThrowException
import javax.inject.Inject


interface LibraryBooksRepository {

    suspend fun issueBookRequest(issueBookRequest : IssueBookRequest) : IssueBookResponse
    suspend fun getBookByLibraryNumber(libraryBookNumber : Long) : CollegeBook

}

class LibraryBooksRepositoryImpl @Inject constructor(
    private val collegeAppService: CollegeAppService
) : LibraryBooksRepository {
    override suspend fun issueBookRequest(issueBookRequest: IssueBookRequest): IssueBookResponse {
        return collegeAppService.issueABook(issueBookRequest).dataOrThrowException()
    }

    override suspend fun getBookByLibraryNumber(libraryBookNumber: Long): CollegeBook {
        return collegeAppService.getBookByLibraryNumber(libraryBookNumber).dataOrThrowException()
    }

}