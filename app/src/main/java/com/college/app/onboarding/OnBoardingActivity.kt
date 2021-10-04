package com.college.app.onboarding

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import com.college.app.databinding.ActivitySplashBinding
import com.college.app.theme.CollegeAppTheme
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CollegeAppTheme {
                ProvideWindowInsets {
                    val scaffoldState = rememberScaffoldState()
                    Scaffold(scaffoldState = scaffoldState) {
                        OnBoardingNavGraph(scaffoldState = scaffoldState)
                    }
                }
            }
        }
    }
}