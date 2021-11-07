package com.college.app.ui.main

import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.college.app.ui.main.home.HomeScreen
import com.college.app.ui.todo.TodoScreen


sealed class HomeBottomDestinations(var route: String) {
    object Home : HomeBottomDestinations("bottom_home")
    object Todo : HomeBottomDestinations("bottom_todo")
}


@Composable
fun MainScreen(
    navHostController: NavHostController
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = listOf(
                    BottomNavigationBarItem(
                        name = "Home",
                        route = HomeBottomDestinations.Home.route,
                        icon = Icons.Default.Home
                    ),
                    BottomNavigationBarItem(
                        name = "Todo",
                        route = HomeBottomDestinations.Todo.route,
                        icon = Icons.Default.Checklist
                    )
                ),
                navController = navHostController,
                onItemClick = {
                    navHostController.navigate(it.route)
                }
            )
        }
    ) {
        MainBottomNavigation(navHostController = navHostController)
    }
}

@Composable
fun MainBottomNavigation(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = HomeBottomDestinations.Home.route
    ) {
        composable(HomeBottomDestinations.Home.route) {
            HomeScreen()
        }
        composable(HomeBottomDestinations.Todo.route) {
            TodoScreen()
        }
    }
}