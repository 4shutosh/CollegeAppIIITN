package com.college.app.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.college.app.nav.CollegeDestinations
import com.college.app.nav.CollegeNavGraph
import com.college.app.theme.CollegeAppTheme
import com.college.app.ui.main.MainActivity
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {

    private val viewModel by viewModels<OnBoardingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setUpViews()
        setUpObservers()
    }

    private fun setUpViews() {
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (viewModel.onBoardingViewState.value?.isReady == true) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else false
                }
            }
        )
    }

    private fun setUpObservers() {
        val content: View = findViewById(android.R.id.content)
        viewModel.onBoardingViewState.observe(this) { viewState ->
            if (viewState.isReady && !viewState.loggedIn) {
                content.viewTreeObserver.dispatchOnPreDraw()
                setContent {
                    CollegeAppTheme {
                        ProvideWindowInsets {
                            val scaffoldState = rememberScaffoldState()
                            Scaffold(scaffoldState = scaffoldState) {
                                rememberSystemUiController().setSystemBarsColor(
                                    color = MaterialTheme.colors.surface
                                )
                                CollegeNavGraph(
                                    scaffoldState = scaffoldState, startDestination =
                                    CollegeDestinations.OnBoardingGraph
                                )
                            }
                        }
                    }
                }
            } else if (viewState.isReady && viewState.loggedIn) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }
}