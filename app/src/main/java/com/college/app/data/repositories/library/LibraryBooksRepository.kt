package com.college.app.data.repositories.library

import com.college.app.models.local.CollegeBook
import com.college.app.network.CollegeAppService
import com.college.app.models.network.requests.IssueBookRequest
import com.college.app.models.network.responses.IssueBookResponse
import com.college.app.models.network.responses.UserLibraryItemResponse
import com.college.base.domain.dataOrThrowException
import javax.inject.Inject


interface LibraryBooksRepository {

    suspend fun issueBookRequest(issueBookRequest: IssueBookRequest): IssueBookResponse
    suspend fun getBookByLibraryNumber(libraryBookNumber: Long): CollegeBook

    suspend fun getIssuedBookForUser(userId: String): List<UserLibraryItemResponse>
}

class LibraryBooksRepositoryImpl @Inject constructor(
    private val collegeAppService: CollegeAppService,
) : LibraryBooksRepository {
    override suspend fun issueBookRequest(issueBookRequest: IssueBookRequest): IssueBookResponse {
        return collegeAppService.issueABook(issueBookRequest).dataOrThrowException()
    }

    override suspend fun getBookByLibraryNumber(libraryBookNumber: Long): CollegeBook {
        return collegeAppService.getBookByLibraryNumber(libraryBookNumber).dataOrThrowException()
    }

    override suspend fun getIssuedBookForUser(userId: String): List<UserLibraryItemResponse> {
        return collegeAppService.getIssuedBooks(userId).dataOrThrowException()
    }

}