package com.college.app.ui.main.home

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun HomeScreen(
    navController: NavController
) {

    Scaffold {
        Text(text = "Hello Main Screen")
    }
}