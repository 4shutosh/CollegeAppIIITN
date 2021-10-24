package com.college.app.network.login

import android.content.Context
import com.college.app.R
import com.college.base.logger.CollegeLogger
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GoogleLogOutHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val logger: CollegeLogger
) {

    private val googleSingInOptions: GoogleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

    private val googleSignInOptions: GoogleSignInClient =
        GoogleSignIn.getClient(context, googleSingInOptions)


    fun signOutAndClearPreferences() {
        googleSignInOptions.signOut().addOnCompleteListener {
            logger.d("wrong email logged out!")
        }
    }


}