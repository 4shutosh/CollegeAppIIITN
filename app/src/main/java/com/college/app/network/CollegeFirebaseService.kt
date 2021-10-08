package com.college.app.network

import com.college.base.logger.CollegeLogger
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface CollegeFirebaseService {
    suspend fun checkForUser(googleUserIdToken: String): Boolean
}

class CollegeFirebaseServiceImpl @Inject constructor(
    private val collegeLogger: CollegeLogger
) : CollegeFirebaseService {

    override suspend fun checkForUser(googleUserIdToken: String): Boolean {
        val credential = GoogleAuthProvider.getCredential(googleUserIdToken, null)
        var result = false
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result = true
                    // todo complete here
                } else {
                    result = false
                    collegeLogger.e("user check for login failed with exception : ${it.exception}")
                }
            }.addOnFailureListener {
                result = false
                collegeLogger.e("user check for login failed with exception : $it")
            }.await()
        return result
    }

}