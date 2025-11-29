package com.example.rmsjims.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainApp(
    startDestination: String = Screen.SharedNavGraph.route
) {
    val navController = rememberNavController()
    MainAppNavGraph(
        navController = navController,
        startDestination = startDestination
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainAppNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.SharedNavGraph.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Shared Navigation Graph - contains Splash, Login, Role Selection
        composable(Screen.SharedNavGraph.route) {
            SharedNavGraph(
                parentNavController = navController
            )
        }

        // Admin Navigation Graph - merged with shared screens
        composable(Screen.AdminNavGraph.route) {
            AdminNavGraph(navController = navController)
        }

        // Assistant Navigation Graph - merged with shared screens
        composable(Screen.AssistantNavGraph.route) {
            AssistantNavGraph(navController = navController)
        }

        // Staff Navigation Graph - merged with shared screens
        composable(Screen.StaffNavGraph.route) {
            StaffNavGraph(navController = navController)
        }
    }
}

@Composable
fun AdminNavGraph(
    navController: NavHostController
) {
    AdminModuleApp()
}

@Composable
fun AssistantNavGraph(
    navController: NavHostController
) {
    AssistantModuleApp()
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StaffNavGraph(
    navController: NavHostController
) {
    StaffModuleApp()
}

