package com.college.app.ui.onboarding

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.college.app.ui.onboarding.OnBoardingDestinations.Login
import com.college.app.ui.onboarding.OnBoardingDestinations.Splash
import com.college.app.ui.onboarding.login.OnBoardingLogin
import com.college.app.ui.onboarding.splash.OnBoardingSplash

sealed class OnBoardingDestinations(var route: String) {
    object Splash : OnBoardingDestinations("splash")
    object Login : OnBoardingDestinations("login")
}

@Composable
fun OnBoardingNavGraph(
    scaffoldState: ScaffoldState,
    navHostController: NavHostController = rememberNavController(),
    startDestination: OnBoardingDestinations = Splash
) {

    NavHost(navController = navHostController, startDestination = startDestination.route) {
        composable(Splash.route) {
            OnBoardingSplash()
        }
        composable(Login.route) {
            OnBoardingLogin()
        }
    }

}