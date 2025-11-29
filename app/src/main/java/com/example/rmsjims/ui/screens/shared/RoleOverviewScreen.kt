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
import androidx.compose.runtime.mutableStateOf
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
fun RoleOverviewScreen(
    navController: NavHostController
) {
    val selectedRole = remember { mutableStateOf<UserRole?>(null) }
    val roles = remember {
        listOf(
            RoleOverview(
                role = UserRole.ADMIN,
                title = "Administrator",
                summary = "Configure systems, manage users, and oversee approvals.",
                focus = "Strategy & Oversight"
            ),
            RoleOverview(
                role = UserRole.STAFF,
                title = "Staff",
                summary = "Handle maintenance workflows, bookings, and approvals.",
                focus = "Operations & Execution"
            ),
            RoleOverview(
                role = UserRole.ASSISTANT,
                title = "Assistant",
                summary = "Monitor resources, log usage, and support daily operations.",
                focus = "Monitoring & Support"
            )
        )
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Choose Your Role",
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
                header = "Select the role that matches your responsibilities.",
                headerColor = onSurfaceColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = ResponsiveLayout.getResponsiveFontSize(18.sp, 20.sp, 22.sp)
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = ResponsiveLayout.getResponsivePadding(12.dp, 16.dp, 20.dp)),
                verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getResponsivePadding(12.dp, 16.dp, 20.dp))
            ) {
                items(roles) { role ->
                    RoleOverviewCard(
                        role = role,
                        isSelected = selectedRole.value == role.role,
                        onSelect = { selectedRole.value = role.role }
                    )
                }
            }
            AppButton(
                onClick = {
                    val role = selectedRole.value?.name ?: UserRole.ASSISTANT.name
                    navController.navigate(Screen.PermissionsOverviewScreen.createRoute(role))
                },
                buttonText = "Review Permissions"
            )
            AppButton(
                onClick = {
                    navController.navigate(Screen.LoginScreen.route)
                },
                buttonText = "Go to Login",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun RoleOverviewCard(
    role: RoleOverview,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onSelect,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) onSurfaceColor.copy(alpha = 0.08f) else onSurfaceVariant
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
            verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getResponsivePadding(8.dp, 10.dp, 12.dp))
        ) {
            CustomLabel(
                header = role.title,
                headerColor = onSurfaceColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = ResponsiveLayout.getResponsiveFontSize(18.sp, 20.sp, 22.sp)
            )
            CustomLabel(
                header = role.summary,
                headerColor = onSurfaceColor.copy(alpha = 0.7f),
                fontSize = ResponsiveLayout.getResponsiveFontSize(13.sp, 14.sp, 15.sp)
            )
            CustomLabel(
                header = "Primary Focus: ${role.focus}",
                headerColor = onSurfaceColor.copy(alpha = 0.8f),
                fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 13.sp, 14.sp)
            )
        }
    }
}

private data class RoleOverview(
    val role: UserRole,
    val title: String,
    val summary: String,
    val focus: String
)

