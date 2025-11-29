package com.example.rmsjims.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rmsjims.ui.screens.staff.BookingScreen
import com.example.rmsjims.ui.screens.staff.CalendarScreen
import com.example.rmsjims.ui.screens.staff.HomeScreen
import com.example.rmsjims.ui.screens.staff.ProdDescScreen
import com.example.rmsjims.ui.screens.staff.ProfileScreen
import com.example.rmsjims.ui.screens.staff.ProjectInfoScreen
import com.example.rmsjims.ui.screens.staff.RaiseTicketScreen
import com.example.rmsjims.ui.screens.assistant.TicketScreen
import com.example.rmsjims.ui.screens.staff.EquipmentScreen
import com.example.rmsjims.viewmodel.BookingScreenViewmodel
import com.example.rmsjims.viewmodel.CalendarViewModel
import com.example.rmsjims.viewmodel.UserSessionViewModel
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StaffModuleApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.RaiseTicketScreen.route
    ) {
        // Staff-specific screens (with bottom navigation)
        composable(Screen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(
            Screen.EquipmentScreen.route,
            arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: "Equipments"
            EquipmentScreen(
                navController = navController,
                categoryName = categoryName
            )
        }
        composable(Screen.BookingsScreen.route) {
            BookingScreen(navController = navController)
        }
        composable(Screen.ProfileScreen.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screen.CalendarScreen.route) {
            val calendarViewModel: CalendarViewModel = koinViewModel()
            val bookingViewModel: BookingScreenViewmodel = koinViewModel()
            CalendarScreen(
                navController = navController,
                viewModel = calendarViewModel,
                bookingViewmodel = bookingViewModel
            )
        }

        // Shared screens - merged directly into StaffNavGraph
        composable(Screen.ProductDescriptionScreen.route) {
            val sessionViewModel: UserSessionViewModel = koinViewModel()
            ProdDescScreen(navController = navController, sessionViewModel = sessionViewModel)
        }
        composable(Screen.ProjectInfoScreen.route) {
            ProjectInfoScreen(navController = navController)
        }
        composable(Screen.RaiseTicketScreen.route) {
            RaiseTicketScreen(navController = navController)
        }
        composable(Screen.TicketScreen.route) {
            TicketScreen()
        }
    }
}
