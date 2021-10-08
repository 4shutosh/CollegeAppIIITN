package com.college.app.data

import com.college.app.network.CollegeFirebaseService
import javax.inject.Inject

interface LoginRepository {
    suspend fun checkForUser(userIdToken: String): Boolean
}

class LoginRepositoryImpl @Inject constructor(
    private val firebaseService: CollegeFirebaseService
) : LoginRepository {
    override suspend fun checkForUser(userIdToken: String): Boolean {
        return firebaseService.checkForUser(userIdToken)
    }

}