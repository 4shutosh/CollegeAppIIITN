package com.college.app.ui.main

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.college.app.nav.CollegeDestinations
import com.college.app.theme.getAppColorScheme

sealed class MainGraphDestinations(var route: String) {
    object Main : MainGraphDestinations("main")
}

data class BottomNavigationBarItem(
    val name: String,
    val route: String,
    val icon: ImageVector
)

fun NavGraphBuilder.mainNavGraph(
    navHostController: NavHostController,
    scaffoldState: ScaffoldState
) {
    navigation(
        startDestination = MainGraphDestinations.Main.route,
        route = CollegeDestinations.HomeGraph.route
    ) {
        composable(MainGraphDestinations.Main.route) {
            MainScreen(navHostController = rememberNavController())
        }
    }
}

@Composable
fun BottomNavigationBar(
    items: List<BottomNavigationBarItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavigationBarItem) -> Unit
) {

    val colorScheme = getAppColorScheme(isSystemInDarkTheme())

    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomNavigation(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 5.dp
    ) {
        items.forEach { item ->

            val isSelected = item.route == backStackEntry.value?.destination?.route

            BottomNavigationItem(
                selected = isSelected,
                onClick = { onItemClick(item) },
                selectedContentColor = colorScheme.iconColorSelected,
                unselectedContentColor = colorScheme.iconColorUnSelected,
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.name
                    )
                    if (isSelected) {
                        // do something
                    }
                })
        }
    }

}