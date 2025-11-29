package com.example.rmsjims.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rmsjims.ui.screens.shared.AboutAppScreen
import com.example.rmsjims.ui.screens.shared.LoginScreen
import com.example.rmsjims.ui.screens.shared.PermissionsOverviewScreen
import com.example.rmsjims.ui.screens.shared.RoleOverviewScreen
import com.example.rmsjims.ui.screens.shared.RoleSelectionScreen
import com.example.rmsjims.ui.theme.app_background
import com.example.rmsjims.viewmodel.UserSessionViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SharedNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.SplashScreen.route,
    parentNavController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Splash Screen - first screen when app launches
        composable(Screen.SplashScreen.route) {
            SplashScreen(
                onSplashComplete = {
                    // Navigate to Login screen after splash
                    navController.navigate(Screen.LoginScreen.route) {
                        popUpTo(Screen.SplashScreen.route) { inclusive = true }
                    }
                }
            )
        }

        // Login Screen
        composable(Screen.LoginScreen.route) {
            LoginScreen(
                navController = navController
            )
        }

        composable(Screen.AboutAppScreen.route) {
            AboutAppScreen(
                navController = navController
            )
        }

        composable(Screen.RoleOverviewScreen.route) {
            RoleOverviewScreen(
                navController = navController
            )
        }



        // Role Selection Screen
        composable(Screen.RoleSelectionScreen.route) {
            val sessionViewModel: UserSessionViewModel = koinViewModel()
            RoleSelectionScreen(
                navController = parentNavController, // Use parent nav controller to navigate to role graphs
                sessionViewModel = sessionViewModel
            )
        }
    }
}

@Composable
private fun SplashScreen(
    onSplashComplete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(app_background),
        contentAlignment = Alignment.Center
    ) {
        // Splash screen content - simple placeholder
        // The actual splash animation with logo is handled by MainActivity's installSplashScreen()
        // The animated_logo drawable is configured in themes.xml and will be perfectly centered
        // by the Android SplashScreen API. This composable just provides a transition point
        // after the system splash animation completes.
        LaunchedEffect(Unit) {
            delay(2000) // 2 second delay for splash transition
            onSplashComplete()
        }
    }
}

