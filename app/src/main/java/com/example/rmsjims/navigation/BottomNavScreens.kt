 package com.example.rmsjims.navigation

import com.example.rmsjims.R
import com.example.rmsjims.data.model.UserRole

data class BottomNavScreens (
    val route: String,
    val iconResId: Int,
    val navIconDesc : String = "Navigation icon",
    val label: String,
    val hasNews : Boolean = false,
    val badgeCount : Int? = null,
    val selected : Boolean = false,
)

fun getBottomNavItems(userRole: UserRole): List<BottomNavScreens> {
    // First tab: Home (same for all sessions)
    val homeTab = BottomNavScreens(
        route = Screen.HomeScreen.route,
        iconResId = R.drawable.nav_ic_home,
        label = "Home"
    )
    
    // Second tab: Equipment (same for all sessions)
    val equipmentTab = BottomNavScreens(
        route = Screen.EquipmentScreen.createRoute("Equipments"),
        iconResId = R.drawable.nav_ic_equip,
        label = "Equipments"
    )
    
    // Third tab: Changes based on session (strict if-else condition)
    val thirdTab = if (userRole == UserRole.ADMIN) {
        // Admin session: Admin Dashboard
        BottomNavScreens(
            route = Screen.AdminDashboardScreen.route,
            iconResId = R.drawable.ic_dashboard,
            label = "Admin Dashboard"
        )
    } else if (userRole == UserRole.STAFF) {
        // Staff session: Booking Screen
        BottomNavScreens(
            route = Screen.BookingsScreen.route,
            iconResId = R.drawable.nav_ic_bookings,
            label = "Bookings"
        )
    } else if (userRole == UserRole.ASSISTANT) {
        // Assistant session: Assistant Screen
        BottomNavScreens(
            route = Screen.AssistantScreen.route,
            iconResId = R.drawable.ic_aichat,
            label = "Assistant"
        )
    } else {
        // UNASSIGNED or fallback: Booking Screen
        BottomNavScreens(
            route = Screen.BookingsScreen.route,
            iconResId = R.drawable.nav_ic_bookings,
            label = "Bookings"
        )
    }
    
    // Fourth tab: Profile (same for all sessions)
    val profileTab = BottomNavScreens(
        route = Screen.ProfileScreen.route,
        iconResId = R.drawable.ic_user,
        label = "Profile"
    )
    
    return listOf(homeTab, equipmentTab, thirdTab, profileTab)
}
