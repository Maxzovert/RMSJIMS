package com.example.rmsjims.navigation

sealed class Screen(val route : String) {
    object HomeScreen : Screen("home")
    object LoginScreen : Screen("login")
    object EquipmentScreen : Screen("equipment/{categoryName}"){
        fun createRoute(categoryName: String) = "equipment/$categoryName"
    }
    object BookingsScreen : Screen("bookings")
    object CalendarScreen : Screen("calendar")
    object ProjectInfoScreen : Screen("project_info")
    object RaiseTicketScreen : Screen("ticket_raise")
    object TicketScreen : Screen("tickets")
    object ProductDescriptionScreen : Screen("profileDescription")
    object ProductDescriptionEditScreen : Screen("profileDescription/edit")
    object ProfileScreen : Screen("profile")
    object AdminDashboardScreen : Screen("admin_dashboard")
    object AdminBookingsScreen : Screen("admin_bookings")
    object EquipmentManagementScreen : Screen("equipment_management")
    object SystemSettingScreen : Screen("system_setting")
    object UserManagementScreen : Screen("user_management")
    object UserDetailScreen : Screen("user_detail/{userId}") {
        fun createRoute(userId: String) = "user_detail/$userId"
    }
    object AssistantScreen : Screen("assistant")
    object MachineStatusScreen : Screen("machine_status")
    object MachineDetailScreen : Screen("machine_detail/{machineId}") {
        fun createRoute(machineId: String) = "machine_detail/$machineId"
    }
    object MaintenanceApprovalScreen : Screen("maintenance_approval")
    object NewEquipmentScreen : Screen("new_equipment")
    object MaintenanceDetailScreen : Screen("maintenance_detail/{requestId}") {
        fun createRoute(requestId: String) = "maintenance_detail/$requestId"
    }
    object RoleSelectionScreen : Screen("role_selection")
    object RequestDetailsScreen : Screen("request_details/{requestId}") {
        fun createRoute(requestId: String) = "request_details/$requestId"
    }
    object ResourceManagementScreen : Screen("resource_management")
    object TicketManagementScreen : Screen("ticket_management")
    object UsageApprovalScreen : Screen("usage_approval")
    object AboutAppScreen : Screen("about_app")
    object RoleOverviewScreen : Screen("role_overview")
    object PermissionsOverviewScreen : Screen("permissions_overview/{selectedRole}") {
        fun createRoute(selectedRole: String) = "permissions_overview/$selectedRole"
    }
    object SplashScreen : Screen("splash")
    
    // Navigation graph routes
    object SharedNavGraph : Screen("shared_nav_graph")
    object AdminNavGraph : Screen("admin_nav_graph")
    object AssistantNavGraph : Screen("assistant_nav_graph")
    object StaffNavGraph : Screen("staff_nav_graph")

}