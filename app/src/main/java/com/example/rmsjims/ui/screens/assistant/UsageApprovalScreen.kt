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
fun UsageApprovalScreen(
    navController: NavHostController
) {
    // Placeholder usage requests
    val usageRequests = listOf(
        UsageRequest(
            equipmentName = "High-Performance Server",
            equipmentId = "EQ-2024-001",
            requestedBy = "Dr. Ravi Kumar",
            requestDate = "2024-01-20",
            startDate = "2024-01-25",
            endDate = "2024-02-05",
            purpose = "Research project on machine learning algorithms",
            priority = "High"
        ),
        UsageRequest(
            equipmentName = "Oscilloscope Pro",
            equipmentId = "EQ-2024-045",
            requestedBy = "Prof. Meera Sharma",
            requestDate = "2024-01-19",
            startDate = "2024-01-22",
            endDate = "2024-01-28",
            purpose = "Circuit analysis for electronics lab",
            priority = "Medium"
        ),
        UsageRequest(
            equipmentName = "3D Printer XL",
            equipmentId = "EQ-2024-089",
            requestedBy = "Dr. Amit Patel",
            requestDate = "2024-01-18",
            startDate = "2024-01-21",
            endDate = "2024-01-25",
            purpose = "Prototype development for mechanical engineering project",
            priority = "Low"
        ),
        UsageRequest(
            equipmentName = "Spectrometer",
            equipmentId = "EQ-2024-123",
            requestedBy = "Sunita Reddy",
            requestDate = "2024-01-17",
            startDate = "2024-01-20",
            endDate = "2024-01-30",
            purpose = "Material analysis for chemistry research",
            priority = "High"
        )
    )

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Usage Approval",
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
            
            // Summary Card
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
                        header = "Pending Approvals",
                        fontSize = ResponsiveLayout.getResponsiveFontSize(14.sp, 16.sp, 18.sp),
                        headerColor = onSurfaceColor.copy(0.7f)
                    )
                    Spacer(modifier = Modifier.height(pxToDp(8)))
                    CustomLabel(
                        header = "${usageRequests.size} Requests",
                        fontSize = ResponsiveLayout.getResponsiveFontSize(20.sp, 24.sp, 28.sp),
                        headerColor = primaryColor
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(ResponsiveLayout.getVerticalPadding()))
            
            // Requests List
            LazyColumn(
                contentPadding = PaddingValues(
                    horizontal = ResponsiveLayout.getHorizontalPadding(),
                    vertical = ResponsiveLayout.getVerticalPadding()
                ),
                verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getCardSpacing())
            ) {
                items(usageRequests) { request ->
                    UsageRequestCard(request = request)
                }
            }
        }
    }
}

@Composable
fun UsageRequestCard(request: UsageRequest) {
    val priorityColor = when (request.priority) {
        "High" -> Color(0xFFE64646)
        "Medium" -> Color(0xFFE67824)
        "Low" -> Color(0xFF26BB64C)
        else -> onSurfaceColor
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = onSurfaceVariant
        ),
        shape = RectangleShape
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp),
                vertical = ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp)
            ),
            verticalArrangement = Arrangement.spacedBy(pxToDp(12))
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(pxToDp(4))
                ) {
                    CustomLabel(
                        header = request.equipmentName,
                        fontSize = ResponsiveLayout.getResponsiveFontSize(16.sp, 18.sp, 20.sp),
                        headerColor = onSurfaceColor
                    )
                    CustomLabel(
                        header = "ID: ${request.equipmentId}",
                        fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                        headerColor = onSurfaceColor.copy(0.6f)
                    )
                }
                
                Box(
                    modifier = Modifier
                        .background(
                            color = priorityColor.copy(0.2f),
                            shape = RoundedCornerShape(pxToDp(12))
                        )
                        .padding(
                            horizontal = pxToDp(12),
                            vertical = pxToDp(6)
                        )
                ) {
                    CustomLabel(
                        header = request.priority,
                        fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                        headerColor = priorityColor
                    )
                }
            }
            
            // Details
            Column(
                verticalArrangement = Arrangement.spacedBy(pxToDp(8))
            ) {
                DetailRow("Requested By", request.requestedBy)
                DetailRow("Request Date", request.requestDate)
                DetailRow("Start Date", request.startDate)
                DetailRow("End Date", request.endDate)
                CustomLabel(
                    header = "Purpose:",
                    fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                    headerColor = onSurfaceColor.copy(0.7f)
                )
                CustomLabel(
                    header = request.purpose,
                    fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                    headerColor = onSurfaceColor
                )
            }
            
            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(pxToDp(12))
            ) {
                AppButton(
                    buttonText = "Approve",
                    onClick = { },
                    modifier = Modifier.weight(1f)
                )
                AppButton(
                    buttonText = "Reject",
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    containerColor = Color(0xFFE64646),
                    contentColor = whiteColor
                )
                AppButton(
                    buttonText = "View Details",
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    containerColor = onSurfaceVariant,
                    contentColor = onSurfaceColor
                )
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CustomLabel(
            header = "$label:",
            fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
            headerColor = onSurfaceColor.copy(0.7f)
        )
        CustomLabel(
            header = value,
            fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
            headerColor = onSurfaceColor
        )
    }
}

// Placeholder data class
data class UsageRequest(
    val equipmentName: String,
    val equipmentId: String,
    val requestedBy: String,
    val requestDate: String,
    val startDate: String,
    val endDate: String,
    val purpose: String,
    val priority: String
)

@Preview(showBackground = true)
@Composable
fun UsageApprovalScreenPreview() {
    val navController = rememberNavController()
    UsageApprovalScreen(navController = navController)
}
