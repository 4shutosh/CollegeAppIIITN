package com.college.app.network

import com.college.app.network.EndPoints.LOGIN
import com.college.app.network.models.responses.LoginResponse
import com.college.app.utils.Constants.Params.EMAIL
import com.college.app.utils.Constants.Params.IMAGE_URL
import com.college.app.utils.Constants.Params.NAME
import com.college.base.domain.ServerResponse
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

}