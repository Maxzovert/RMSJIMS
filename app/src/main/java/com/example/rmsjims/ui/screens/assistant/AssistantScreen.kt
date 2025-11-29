package com.example.rmsjims.ui.screens.assistant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rmsjims.ui.components.AppButton
import com.example.rmsjims.ui.components.CustomLabel
import com.example.rmsjims.ui.components.CustomNavigationBar
import com.example.rmsjims.ui.components.CustomTopBar
import com.example.rmsjims.ui.theme.onSurfaceColor
import com.example.rmsjims.ui.theme.onSurfaceVariant
import com.example.rmsjims.ui.theme.primaryColor
import com.example.rmsjims.ui.theme.whiteColor
import com.example.rmsjims.util.ResponsiveLayout
import com.example.rmsjims.util.pxToDp

@Composable
fun AssistantScreen(
    navController: NavHostController
) {
    // Placeholder tasks
    val tasks = listOf(
        AssistantTask(
            title = "Pending Approvals",
            count = 5,
            description = "Review and approve equipment usage requests",
            color = Color(0xFFE67824)
        ),
        AssistantTask(
            title = "Machine Maintenance",
            count = 3,
            description = "Schedule and track equipment maintenance",
            color = Color(0xFFE64646)
        ),
        AssistantTask(
            title = "New Equipment",
            count = 2,
            description = "Register and configure new equipment",
            color = Color(0xFF26BB64C)
        ),
        AssistantTask(
            title = "Ticket Management",
            count = 8,
            description = "Handle equipment-related support tickets",
            color = primaryColor
        )
    )

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Lab Assistant",
                onNavigationClick = { navController.popBackStack() },
                navController = navController
            )
        },
        bottomBar = {
            CustomNavigationBar(navController = navController)
        },
        containerColor = whiteColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(ResponsiveLayout.getVerticalPadding()))
            
            // Welcome Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ResponsiveLayout.getHorizontalPadding()),
                colors = CardDefaults.cardColors(
                    containerColor = onSurfaceVariant
                ),
                shape = RectangleShape
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = ResponsiveLayout.getHorizontalPadding(),
                        vertical = ResponsiveLayout.getVerticalPadding()
                    )
                ) {
                    CustomLabel(
                        header = "Welcome, Lab Assistant",
                        fontSize = ResponsiveLayout.getResponsiveFontSize(20.sp, 24.sp, 28.sp),
                        headerColor = onSurfaceColor
                    )
                    Spacer(modifier = Modifier.height(pxToDp(8)))
                    CustomLabel(
                        header = "Manage equipment and assist users efficiently",
                        fontSize = ResponsiveLayout.getResponsiveFontSize(14.sp, 16.sp, 18.sp),
                        headerColor = onSurfaceColor.copy(0.7f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(ResponsiveLayout.getVerticalPadding()))
            
            // Tasks List
            CustomLabel(
                header = "Quick Tasks",
                fontSize = ResponsiveLayout.getResponsiveFontSize(18.sp, 20.sp, 22.sp),
                modifier = Modifier.padding(horizontal = ResponsiveLayout.getHorizontalPadding()),
                headerColor = onSurfaceColor
            )
            
            Spacer(modifier = Modifier.height(ResponsiveLayout.getResponsivePadding(12.dp, 16.dp, 20.dp)))
            
            LazyColumn(
                contentPadding = PaddingValues(
                    horizontal = ResponsiveLayout.getHorizontalPadding(),
                    vertical = ResponsiveLayout.getVerticalPadding()
                ),
                verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getCardSpacing())
            ) {
                items(tasks) { task ->
                    TaskCard(task = task)
                }
            }
        }
    }
}

@Composable
fun TaskCard(task: AssistantTask) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = onSurfaceVariant
        ),
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp),
                    vertical = ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp)
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(pxToDp(8))
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(pxToDp(12)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomLabel(
                        header = task.title,
                        fontSize = ResponsiveLayout.getResponsiveFontSize(16.sp, 18.sp, 20.sp),
                        headerColor = onSurfaceColor
                    )
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier
                            .background(
                                color = task.color.copy(0.2f),
                                shape = RoundedCornerShape(pxToDp(12))
                            )
                            .padding(
                                horizontal = pxToDp(8),
                                vertical = pxToDp(4)
                            )
                    ) {
                        CustomLabel(
                            header = "${task.count}",
                            fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                            headerColor = task.color
                        )
                    }
                }
                CustomLabel(
                    header = task.description,
                    fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                    headerColor = onSurfaceColor.copy(0.6f)
                )
            }
            
            AppButton(
                buttonText = "View",
                onClick = { },
                modifier = Modifier.padding(start = pxToDp(16))
            )
        }
    }
}

// Placeholder data class
data class AssistantTask(
    val title: String,
    val count: Int,
    val description: String,
    val color: Color
)

@Preview(showBackground = true)
@Composable
fun AssistantScreenPreview() {
    val navController = rememberNavController()
    AssistantScreen(navController = navController)
}
