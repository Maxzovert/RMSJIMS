package com.example.rmsjims.ui.screens.assistant

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.rmsjims.R
import com.example.rmsjims.data.model.MaintenanceStatusType
import com.example.rmsjims.data.model.MaintenanceTab
import com.example.rmsjims.data.model.MaintenanceTabItem
import com.example.rmsjims.navigation.Screen
import com.example.rmsjims.ui.components.AppCategoryIcon
import com.example.rmsjims.ui.components.AppNavIcon
import com.example.rmsjims.ui.components.CustomLabel
import com.example.rmsjims.ui.components.CustomNavigationBar
import com.example.rmsjims.ui.components.CustomTopBar
import com.example.rmsjims.ui.theme.categoryIconColor
import com.example.rmsjims.ui.theme.lightTextColor
import com.example.rmsjims.ui.theme.navLabelColor
import com.example.rmsjims.ui.theme.onSurfaceColor
import com.example.rmsjims.ui.theme.onSurfaceVariant
import com.example.rmsjims.ui.theme.primaryColor
import com.example.rmsjims.ui.theme.whiteColor
import com.example.rmsjims.util.ResponsiveLayout
import com.example.rmsjims.util.pxToDp

@Composable
fun MaintenanceApprovalScreen(
    navController: NavHostController
) {
    var selectedTab by remember { mutableStateOf(MaintenanceTab.Pending) }
    
    // Placeholder maintenance requests data
    val allRequests = listOf(
        MaintenanceRequestData(
            id = "MAINT-001",
            equipmentName = "3D Printer XL",
            equipmentId = "EQ-2024-089",
            imageUrl = R.drawable.temp,
            facilityName = "Makerspace",
            location = "Lab C-301",
            status = MaintenanceStatusType.PENDING,
            requestedBy = "Dr. Ravi Kumar",
            requesterLocation = "Computer Science Department",
            requestDate = "2024-01-20",
            urgency = "High",
            description = "Printer head needs replacement. Calibration issues reported.",
            estimatedCost = "₹15,000",
            deadline = "2024-01-25",
            requestType = "Issue",
            summary = "Printer head replacement required"
        ),
        MaintenanceRequestData(
            id = "MAINT-002",
            equipmentName = "Oscilloscope Pro",
            equipmentId = "EQ-2024-045",
            imageUrl = R.drawable.temp,
            facilityName = "Electronics Lab",
            location = "Lab B-205",
            status = MaintenanceStatusType.APPROVED,
            requestedBy = "Prof. Meera Sharma",
            requesterLocation = "Electronics Department",
            requestDate = "2024-01-19",
            urgency = "Medium",
            description = "Regular maintenance and calibration check.",
            estimatedCost = "₹5,000",
            deadline = "2024-02-01",
            requestType = "Project",
            summary = "Scheduled maintenance for oscilloscope"
        ),
        MaintenanceRequestData(
            id = "MAINT-003",
            equipmentName = "High-Performance Server",
            equipmentId = "EQ-2024-001",
            imageUrl = R.drawable.temp,
            facilityName = "IDC, Photo Studio",
            location = "Lab A-101",
            status = MaintenanceStatusType.IN_PROGRESS,
            requestedBy = "Akash Singh",
            requesterLocation = "IT Department",
            requestDate = "2024-01-18",
            urgency = "High",
            description = "Server overheating. Cooling system needs repair.",
            estimatedCost = "₹25,000",
            deadline = "2024-01-22",
            requestType = "Issue",
            summary = "Cooling system repair urgent"
        ),
        MaintenanceRequestData(
            id = "MAINT-004",
            equipmentName = "Spectrometer",
            equipmentId = "EQ-2024-123",
            imageUrl = R.drawable.temp,
            facilityName = "Chemistry Lab",
            location = "Lab D-405",
            status = MaintenanceStatusType.REJECTED,
            requestedBy = "Dr. Amit Patel",
            requesterLocation = "Chemistry Department",
            requestDate = "2024-01-17",
            urgency = "Low",
            description = "Regular calibration requested.",
            estimatedCost = "₹3,000",
            deadline = "2024-02-15",
            requestType = "Project",
            summary = "Routine calibration check"
        )
    )
    
    // Filter requests based on selected tab
    val filteredRequests = when (selectedTab) {
        MaintenanceTab.Pending -> allRequests.filter { it.status == MaintenanceStatusType.PENDING }
        MaintenanceTab.Approved -> allRequests.filter { it.status == MaintenanceStatusType.APPROVED }
        MaintenanceTab.Rejected -> allRequests.filter { it.status == MaintenanceStatusType.REJECTED }
        MaintenanceTab.In_Progress -> allRequests.filter { 
            it.status == MaintenanceStatusType.IN_PROGRESS || it.status == MaintenanceStatusType.COMPLETE
        }
    }
    
    val tabs = listOf(
        MaintenanceTabItem(MaintenanceTab.Pending, "Pending", R.drawable.ic_booking_pending, selectedTab == MaintenanceTab.Pending),
        MaintenanceTabItem(MaintenanceTab.Approved, "Approved", R.drawable.ic_booking_verified, selectedTab == MaintenanceTab.Approved),
        MaintenanceTabItem(MaintenanceTab.Rejected, "Rejected", R.drawable.ic_booking_canceled, selectedTab == MaintenanceTab.Rejected),
        MaintenanceTabItem(MaintenanceTab.In_Progress, "In Progress", R.drawable.ic_assigned_time, selectedTab == MaintenanceTab.In_Progress)
    )

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Maintenance Approval",
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
            Spacer(modifier = Modifier.height(ResponsiveLayout.getResponsivePadding(27.dp, 32.dp, 38.dp)))
            
            // Tab Selector
            MaintenanceTabSelector(
                tabs = tabs,
                onTabSelected = { selectedTab = it }
            )
            
            Spacer(modifier = Modifier.height(ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp)))
            
            // Equipment Cards List
            LazyColumn(
                contentPadding = PaddingValues(
                    horizontal = ResponsiveLayout.getHorizontalPadding(),
                    vertical = ResponsiveLayout.getVerticalPadding()
                ),
                verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getCardSpacing())
            ) {
                items(filteredRequests) { request ->
                    MaintenanceEquipmentCard(
                        request = request,
                        onClick = {
                            navController.navigate(Screen.MaintenanceDetailScreen.createRoute(request.id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MaintenanceTabSelector(
    tabs: List<MaintenanceTabItem>,
    onTabSelected: (MaintenanceTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = ResponsiveLayout.getHorizontalPadding()),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        tabs.forEach { tabItem ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .clickable { onTabSelected(tabItem.tab) }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(pxToDp(10))
                ) {
                    AppNavIcon(
                        painter = painterResource(id = tabItem.iconRes),
                        iconDescription = tabItem.label,
                        iconSize = pxToDp(20),
                        tint = if (tabItem.isSelected) primaryColor else categoryIconColor
                    )
                    CustomLabel(
                        header = tabItem.label,
                        fontSize = 12.sp,
                        headerColor = if (tabItem.isSelected) primaryColor else categoryIconColor
                    )
                }
            }
        }
    }
}

@Composable
fun MaintenanceEquipmentCard(
    request: MaintenanceRequestData,
    onClick: () -> Unit,
    shape: Shape = RectangleShape,
    imageHeight: Dp = ResponsiveLayout.getResponsiveSize(125.dp, 140.dp, 160.dp),
    detailHeight: Dp = ResponsiveLayout.getResponsiveSize(75.dp, 85.dp, 95.dp)
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = shape
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .background(onSurfaceVariant)
                    .height(imageHeight)
                    .fillMaxWidth()
            ) {
                AsyncImage(
                    model = request.imageUrl,
                    contentDescription = "Equipment Image",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(
                            horizontal = ResponsiveLayout.getResponsiveSize(12.dp, 16.dp, 20.dp),
                            vertical = ResponsiveLayout.getResponsiveSize(12.dp, 16.dp, 20.dp)
                        ),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .height(detailHeight)
                    .fillMaxWidth()
                    .background(whiteColor)
                    .padding(
                        horizontal = ResponsiveLayout.getHorizontalPadding(),
                        vertical = ResponsiveLayout.getResponsiveSize(6.dp, 8.dp, 10.dp)
                    ),
                horizontalAlignment = Alignment.Start
            ) {
                CustomLabel(
                    header = request.equipmentName,
                    headerColor = onSurfaceColor,
                    fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                    modifier = Modifier.padding(
                        vertical = ResponsiveLayout.getResponsiveSize(2.dp, 3.dp, 4.dp)
                    )
                )

                // Status Badge (map IN_PROGRESS to Complete)
                val badgeStatus = when (request.status) {
                    MaintenanceStatusType.IN_PROGRESS -> MaintenanceStatusType.COMPLETE
                    else -> request.status
                }
                Box(
                    modifier = Modifier.padding(
                        vertical = ResponsiveLayout.getResponsiveSize(3.dp, 4.dp, 5.dp)
                    )
                ) {
                    CustomLabel(
                        header = badgeStatus.label,
                        headerColor = whiteColor,
                        fontSize = ResponsiveLayout.getResponsiveFontSize(10.sp, 12.sp, 14.sp),
                        modifier = Modifier
                            .background(
                                color = badgeStatus.color,
                                shape = RoundedCornerShape(pxToDp(12))
                            )
                            .padding(
                                horizontal = pxToDp(8),
                                vertical = pxToDp(4)
                            )
                    )
                }

                CustomLabel(
                    header = request.facilityName,
                    headerColor = lightTextColor,
                    fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                    modifier = Modifier.padding(
                        bottom = ResponsiveLayout.getResponsiveSize(3.dp, 4.dp, 5.dp)
                    )
                )

                Row(
                    modifier = Modifier.padding(
                        top = ResponsiveLayout.getResponsiveSize(3.dp, 4.dp, 5.dp)
                    )
                ) {
                    AppCategoryIcon(
                        painter = painterResource(R.drawable.ic_location),
                        iconDescription = "location icon",
                        iconSize = ResponsiveLayout.getResponsiveSize(12.dp, 14.dp, 16.dp),
                        tint = lightTextColor
                    )
                    Spacer(modifier = Modifier.width(ResponsiveLayout.getResponsiveSize(5.dp, 6.dp, 8.dp)))
                    CustomLabel(
                        header = request.location,
                        headerColor = lightTextColor,
                        fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                        modifier = Modifier.padding(bottom = 0.dp)
                    )
                }
            }
        }
    }
}

// Data class for maintenance request
data class MaintenanceRequestData(
    val id: String,
    val equipmentName: String,
    val equipmentId: String,
    val imageUrl: Int,
    val facilityName: String,
    val location: String,
    val status: MaintenanceStatusType,
    val requestedBy: String,
    val requesterLocation: String,
    val requestDate: String,
    val urgency: String,
    val description: String,
    val estimatedCost: String,
    val deadline: String,
    val requestType: String, // "Project" or "Issue"
    val summary: String
)

@Preview(showBackground = true)
@Composable
fun MaintenanceApprovalScreenPreview() {
    val navController = rememberNavController()
    MaintenanceApprovalScreen(navController = navController)
}
