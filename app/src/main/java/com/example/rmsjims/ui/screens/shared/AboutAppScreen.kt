package com.example.rmsjims.ui.screens.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rmsjims.navigation.Screen
import com.example.rmsjims.ui.components.AppButton
import com.example.rmsjims.ui.components.CustomLabel
import com.example.rmsjims.ui.components.CustomTopBar
import com.example.rmsjims.ui.theme.onSurfaceColor
import com.example.rmsjims.ui.theme.onSurfaceVariant
import com.example.rmsjims.ui.theme.whiteColor
import com.example.rmsjims.util.ResponsiveLayout

@Composable
fun AboutAppScreen(
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            CustomTopBar(
                title = "About RMSJIMS",
                onNavigationClick = null,
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
                header = "Everything you need to manage lab resources.",
                headerColor = onSurfaceColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = ResponsiveLayout.getResponsiveFontSize(20.sp, 22.sp, 24.sp)
            )
            CustomLabel(
                header = "JIMS Resource Management Suite streamlines bookings, usage tracking, maintenance approvals, and team collaboration in one cohesive experience.",
                headerColor = onSurfaceVariant,
                fontSize = ResponsiveLayout.getResponsiveFontSize(13.sp, 14.sp, 15.sp)
            )
            CustomLabel(
                header = "Track equipment availability, assign work, approve requests, and keep every stakeholder in sync — all while enjoying a responsive, modern interface tailored for daily operations.",
                headerColor = onSurfaceVariant,
                fontSize = ResponsiveLayout.getResponsiveFontSize(13.sp, 14.sp, 15.sp)
            )
            Spacer(modifier = Modifier.height(ResponsiveLayout.getResponsivePadding(28.dp, 32.dp, 36.dp)))
            AppButton(
                onClick = {
                    navController.navigate(Screen.RoleOverviewScreen.route)
                },
                buttonText = "Get Started"
            )
            AppButton(
                onClick = {
                    navController.navigate(Screen.LoginScreen.route)
                },
                buttonText = "Skip to Login",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

