package com.college.app.ui.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.college.app.nav.CollegeDestinations
import com.college.app.ui.main.MainGraphDestinations.Home
import com.college.app.ui.main.home.HomeScreen

sealed class MainGraphDestinations(var route: String) {
    object Home : MainGraphDestinations("home")
    object Todo : MainGraphDestinations("todo")
}

fun NavGraphBuilder.mainNavGraph(
    navController: NavController
) {
    navigation(startDestination = Home.route, route = CollegeDestinations.HomeGraph.route) {
        composable(Home.route) {
            HomeScreen(navController = navController)
        }
    }

}