package com.college.app.data.repositories.login

import com.college.app.network.models.requests.LoginRequest
import com.college.app.network.models.responses.LoginResponse
import com.college.app.network.CollegeAppService
import com.college.app.network.CollegeFirebaseService
import com.college.base.domain.dataOrThrowException
import com.college.base.domain.getMoshiAdapterServerResponse
import javax.inject.Inject

interface LoginRepository {
    suspend fun checkForUser(loginRequest: LoginRequest): LoginResponse
}

class LoginRepositoryImpl @Inject constructor(
    private val firebaseService: CollegeFirebaseService,
    private val collegeService: CollegeAppService
) : LoginRepository {

    override suspend fun checkForUser(loginRequest: LoginRequest): LoginResponse {
        return collegeService.loginOrCreateUser(
            email = loginRequest.email,
            name = loginRequest.name,
            imageUrl = loginRequest.imageUrl.orEmpty()
        ).dataOrThrowException()
    }

}