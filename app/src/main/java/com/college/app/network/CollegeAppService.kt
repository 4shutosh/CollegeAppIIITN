package com.college.app.network

import com.college.app.models.network.responses.LoginResponse
import com.college.app.utils.Constants.Params.EMAIL
import com.college.app.utils.Constants.Params.IMAGE_URL
import com.college.app.utils.Constants.Params.NAME
import com.college.app.utils.Constants.Params.USER_ID
import com.college.base.domain.ServerResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CollegeAppService {

    @GET
    fun loginOrCreateUser(
        @Query(NAME) name: String,
        @Query(EMAIL) email: String,
        @Query(IMAGE_URL) imageUrl: String,
    ): ServerResponse<LoginResponse>

}