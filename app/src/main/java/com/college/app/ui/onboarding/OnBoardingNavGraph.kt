package com.college.app.ui.onboarding

import androidx.compose.material.ScaffoldState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.college.app.nav.CollegeDestinations
import com.college.app.ui.onboarding.OnBoardingDestinations.Login
import com.college.app.ui.onboarding.OnBoardingDestinations.Splash
import com.college.app.ui.onboarding.login.OnBoardingLogin
import com.college.app.ui.onboarding.splash.OnBoardingSplash

sealed class OnBoardingDestinations(var route: String) {
    object Splash : OnBoardingDestinations("splash")
    object Login : OnBoardingDestinations("login")
}

fun NavGraphBuilder.onBoardingNavGraph(
    scaffoldState: ScaffoldState,
    navController: NavController
) {
    navigation(
        startDestination = Login.route,
        route = CollegeDestinations.OnBoardingGraph.route
    ) {
        composable(Login.route) {
            OnBoardingLogin(navController, scaffoldState)
        }
        composable(Splash.route) {
            OnBoardingSplash()
        }
    }
}