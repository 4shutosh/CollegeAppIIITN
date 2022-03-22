package com.college.app.network

import com.college.app.models.local.CollegeBook
import com.college.app.network.EndPoints.BOOKS
import com.college.app.network.EndPoints.ISSUE_BOOK
import com.college.app.network.EndPoints.LIBRARY
import com.college.app.network.EndPoints.LOGIN
import com.college.app.models.network.requests.IssueBookRequest
import com.college.app.models.network.responses.IssueBookResponse
import com.college.app.models.network.responses.LoginResponse
import com.college.app.models.network.responses.UserLibraryItemResponse
import com.college.app.utils.Constants.Params.EMAIL
import com.college.app.utils.Constants.Params.IMAGE_URL
import com.college.app.utils.Constants.Params.LIBRARY_BOOK_NUMBER
import com.college.app.utils.Constants.Params.NAME
import com.college.app.utils.Constants.Params.USER_ID
import com.college.base.domain.ServerResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// all the functions here need to be suspend

interface CollegeAppService {

    @POST(LOGIN)
    suspend fun loginOrCreateUser(
        @Query(NAME) name: String,
        @Query(EMAIL) email: String,
        @Query(IMAGE_URL) imageUrl: String,
    ): LoginResponse

    @GET(BOOKS)
    suspend fun getBookByLibraryNumber(
        @Query(LIBRARY_BOOK_NUMBER) libraryBookNumber: Long,
    ): ServerResponse<CollegeBook>

    @POST(ISSUE_BOOK)
    suspend fun issueABook(
        @Body issueBookRequest: IssueBookRequest,
    ): ServerResponse<IssueBookResponse>

    @GET(LIBRARY)
    suspend fun getIssuedBooks(
        @Query(USER_ID) userId: String,
    ): ServerResponse<List<UserLibraryItemResponse>>

}