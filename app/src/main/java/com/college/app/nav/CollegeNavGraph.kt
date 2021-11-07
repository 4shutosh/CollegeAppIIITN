package com.college.app.nav

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.college.app.ui.main.mainNavGraph
import com.college.app.ui.onboarding.onBoardingNavGraph


sealed class CollegeDestinations(var route: String) {
    object OnBoardingGraph : CollegeDestinations("onBoardingGraph")
    object HomeGraph : CollegeDestinations("homeGraph")
}

@Composable
fun CollegeNavGraph(
    scaffoldState: ScaffoldState,
    navHostController: NavHostController = rememberNavController(),
    startDestination: CollegeDestinations = CollegeDestinations.OnBoardingGraph
) {

    NavHost(navController = navHostController, startDestination = startDestination.route) {

        onBoardingNavGraph(navHostController = navHostController, scaffoldState = scaffoldState)

        mainNavGraph(navHostController = navHostController, scaffoldState = scaffoldState)

    }
}
