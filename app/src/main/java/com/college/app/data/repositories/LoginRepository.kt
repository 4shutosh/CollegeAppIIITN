package com.college.app.data.repositories

import com.college.app.models.network.requests.LoginRequest
import com.college.app.models.network.responses.LoginResponse
import com.college.app.network.CollegeAppService
import com.college.app.network.CollegeFirebaseService
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
        )
    }

}