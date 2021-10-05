package com.college.app.onboarding

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.college.app.theme.CollegeAppTheme
import com.google.accompanist.insets.ProvideWindowInsets
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
                    return if (viewModel.isReady) {
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
            if (viewState.isReady) {
                content.viewTreeObserver.dispatchOnPreDraw()
                setContent {
                    CollegeAppTheme {
                        ProvideWindowInsets {
                            val scaffoldState = rememberScaffoldState()
                            Scaffold(scaffoldState = scaffoldState) {
                                OnBoardingNavGraph(
                                    scaffoldState = scaffoldState,
                                    startDestination = viewState.startDestination
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}