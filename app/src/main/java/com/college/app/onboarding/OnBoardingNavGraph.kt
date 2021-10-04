package com.college.app.onboarding

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.college.app.onboarding.OnBoardingDestinations.SPLASH
import com.college.app.onboarding.splash.OnBoardingSplash

object OnBoardingDestinations {
    const val SPLASH = "SPLASH"
    const val LOGIN = "Login"
}

@Composable
fun OnBoardingNavGraph(
    scaffoldState: ScaffoldState,
    navHostController: NavHostController = rememberNavController(),
    startDestination: String = SPLASH
) {

    NavHost(navController = navHostController, startDestination = startDestination) {
        composable(SPLASH) {
            OnBoardingSplash()
        }
    }

}