package com.example.rmsjims.ui.screens.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rmsjims.data.model.UserRole
import com.example.rmsjims.navigation.Screen
import com.example.rmsjims.ui.components.AppButton
import com.example.rmsjims.ui.components.CustomLabel
import com.example.rmsjims.ui.components.CustomTopBar
import com.example.rmsjims.ui.theme.onSurfaceColor
import com.example.rmsjims.ui.theme.onSurfaceVariant
import com.example.rmsjims.ui.theme.whiteColor
import com.example.rmsjims.util.ResponsiveLayout

@Composable
fun PermissionsOverviewScreen(
    navController: NavHostController,
    selectedRole: UserRole?
) {
    val sections = remember {
        listOf(
            RolePermissions(
                role = UserRole.ADMIN,
                title = "Administrator",
                overview = "Complete access to configurations, analytics, and policy management.",
                modules = listOf(
                    "User & Role Management",
                    "System Settings & Audit Logs",
                    "Equipment Assignment & Allocation",
                    "Global Maintenance Approvals"
                )
            ),
            RolePermissions(
                role = UserRole.STAFF,
                title = "Staff",
                overview = "Operational control for maintenance, bookings, and equipment status.",
                modules = listOf(
                    "Maintenance Requests & Approvals",
                    "Booking & Calendar Management",
                    "Usage Approval & Reporting",
                    "Resource Condition Updates"
                )
            ),
            RolePermissions(
                role = UserRole.ASSISTANT,
                title = "Assistant",
                overview = "Support roles for resource monitoring and daily coordination.",
                modules = listOf(
                    "Resource Availability Monitoring",
                    "Usage Recording & Inventory Checks",
                    "Ticket Management Support",
                    "Real-time Alerts & Notifications"
                )
            )
        )
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Roles & Permissions",
                onNavigationClick = { navController.popBackStack() },
                navController = navController
            )
        },
        containerColor = whiteColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = ResponsiveLayout.getHorizontalPadding()),
            verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getResponsivePadding(18.dp, 22.dp, 26.dp)),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(ResponsiveLayout.getVerticalPadding()))
            CustomLabel(
                header = "Understand what each role can access before you log in.",
                headerColor = onSurfaceColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = ResponsiveLayout.getResponsiveFontSize(18.sp, 20.sp, 22.sp)
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = ResponsiveLayout.getResponsivePadding(12.dp, 16.dp, 20.dp)),
                verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getResponsivePadding(12.dp, 16.dp, 20.dp))
            ) {
                items(sections) { section ->
                    PermissionsCard(
                        section = section,
                        isHighlighted = selectedRole == section.role
                    )
                }
            }
            AppButton(
                onClick = {
                    navController.navigate(Screen.LoginScreen.route) {
                        popUpTo(Screen.AboutAppScreen.route) { inclusive = false }
                    }
                },
                buttonText = "Continue to Login"
            )
        }
    }
}

@Composable
private fun PermissionsCard(
    section: RolePermissions,
    isHighlighted: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isHighlighted) onSurfaceColor.copy(alpha = 0.08f) else onSurfaceVariant
        ),
        shape = RoundedCornerShape(ResponsiveLayout.getResponsivePadding(14.dp, 18.dp, 22.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = ResponsiveLayout.getResponsivePadding(18.dp, 22.dp, 26.dp),
                    vertical = ResponsiveLayout.getResponsivePadding(20.dp, 24.dp, 28.dp)
                ),
            verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getResponsivePadding(10.dp, 12.dp, 14.dp))
        ) {
            CustomLabel(
                header = section.title,
                headerColor = onSurfaceColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = ResponsiveLayout.getResponsiveFontSize(18.sp, 20.sp, 22.sp)
            )
            CustomLabel(
                header = section.overview,
                headerColor = onSurfaceColor.copy(alpha = 0.7f),
                fontSize = ResponsiveLayout.getResponsiveFontSize(13.sp, 14.sp, 15.sp)
            )
            section.modules.forEach { module ->
                CustomLabel(
                    header = "• $module",
                    headerColor = onSurfaceColor.copy(alpha = 0.85f),
                    fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 13.sp, 14.sp)
                )
            }
        }
    }
}

private data class RolePermissions(
    val role: UserRole,
    val title: String,
    val overview: String,
    val modules: List<String>
)

