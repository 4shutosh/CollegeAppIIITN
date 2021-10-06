package com.college.app.services

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface CollegeFirebaseService {

}

class CollegeFirebaseServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context

) : CollegeFirebaseService {

}