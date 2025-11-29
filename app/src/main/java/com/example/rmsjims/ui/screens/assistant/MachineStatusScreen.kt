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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.rmsjims.data.model.MachineStatusType
import com.example.rmsjims.data.model.MachineTab
import com.example.rmsjims.data.model.MachineTabItem
import com.example.rmsjims.navigation.Screen
import com.example.rmsjims.ui.components.AppCategoryIcon
import com.example.rmsjims.ui.components.AppNavIcon
import com.example.rmsjims.ui.components.CustomLabel
import com.example.rmsjims.ui.components.CustomNavigationBar
import com.example.rmsjims.ui.components.CustomTopBar
import com.example.rmsjims.ui.theme.categoryIconColor
import com.example.rmsjims.ui.theme.lightTextColor
import com.example.rmsjims.ui.theme.onSurfaceColor
import com.example.rmsjims.ui.theme.onSurfaceVariant
import com.example.rmsjims.ui.theme.primaryColor
import com.example.rmsjims.ui.theme.whiteColor
import com.example.rmsjims.util.ResponsiveLayout
import com.example.rmsjims.util.pxToDp

@Composable
fun MachineStatusScreen(
    navController: NavHostController
) {
    var selectedTab by remember { mutableStateOf(MachineTab.Active_Machines) }
    
    // Placeholder machine data with status types
    val allMachines = listOf(
        MachineData(
            id = "EQ-2024-001",
            name = "High-Performance Server",
            statusType = MachineStatusType.OPERATIONAL,
            location = "Lab A-101",
            imageUrl = R.drawable.temp,
            facilityName = "IDC, Photo Studio",
            lastMaintenance = "2024-01-10",
            nextMaintenance = "2024-02-10",
            brand = "Dell",
            model = "PowerEdge R740"
        ),
        MachineData(
            id = "EQ-2024-045",
            name = "Oscilloscope Pro",
            statusType = MachineStatusType.IDLE,
            location = "Lab B-205",
            imageUrl = R.drawable.temp,
            facilityName = "Electronics Lab",
            lastMaintenance = "2024-01-05",
            nextMaintenance = "2024-02-05",
            brand = "Tektronix",
            model = "TBS1000C"
        ),
        MachineData(
            id = "EQ-2024-089",
            name = "3D Printer XL",
            statusType = MachineStatusType.UNDER_SCHEDULE_MAINTENANCE,
            location = "Lab C-301",
            imageUrl = R.drawable.temp,
            facilityName = "Makerspace",
            lastMaintenance = "2024-01-15",
            nextMaintenance = "2024-01-25",
            brand = "Ultimaker",
            model = "S5 Pro"
        ),
        MachineData(
            id = "EQ-2024-123",
            name = "Spectrometer",
            statusType = MachineStatusType.OPERATIONAL,
            location = "Lab D-405",
            imageUrl = R.drawable.temp,
            facilityName = "Chemistry Lab",
            lastMaintenance = "2024-01-12",
            nextMaintenance = "2024-02-12",
            brand = "Agilent",
            model = "8453"
        ),
        MachineData(
            id = "EQ-2024-156",
            name = "Microscope Advanced",
            statusType = MachineStatusType.IDLE,
            location = "Lab E-502",
            imageUrl = R.drawable.temp,
            facilityName = "Biology Lab",
            lastMaintenance = "2024-01-08",
            nextMaintenance = "2024-02-08",
            brand = "Nikon",
            model = "Eclipse Ti2"
        ),
        MachineData(
            id = "EQ-2024-200",
            name = "Centrifuge Ultra",
            statusType = MachineStatusType.MARK_AS_OUT_OF_ORDER,
            location = "Lab F-601",
            imageUrl = R.drawable.temp,
            facilityName = "Biotech Lab",
            lastMaintenance = "2024-01-01",
            nextMaintenance = "2024-03-01",
            brand = "Eppendorf",
            model = "5424 R"
        ),
        MachineData(
            id = "EQ-2024-201",
            name = "DNA Sequencer",
            statusType = MachineStatusType.UNDER_CHECK,
            location = "Lab G-705",
            imageUrl = R.drawable.temp,
            facilityName = "Genomics Lab",
            lastMaintenance = "2024-01-20",
            nextMaintenance = "2024-02-20",
            brand = "Illumina",
            model = "MiSeq"
        ),
        MachineData(
            id = "EQ-2024-202",
            name = "Thermal Cycler",
            statusType = MachineStatusType.OFFLINE,
            location = "Lab H-808",
            imageUrl = R.drawable.temp,
            facilityName = "Molecular Lab",
            lastMaintenance = "2023-12-15",
            nextMaintenance = "2024-02-15",
            brand = "Applied Biosystems",
            model = "Veriti"
        )
    )
    
    // Filter machines based on selected tab
    val filteredMachines = when (selectedTab) {
        MachineTab.Active_Machines -> allMachines.filter { 
            it.statusType == MachineStatusType.OPERATIONAL || it.statusType == MachineStatusType.IDLE 
        }
        MachineTab.Under_Maintenance -> allMachines.filter { 
            it.statusType == MachineStatusType.UNDER_SCHEDULE_MAINTENANCE || 
            it.statusType == MachineStatusType.UNDER_CHECK 
        }
        MachineTab.Available -> allMachines.filter { 
            it.statusType == MachineStatusType.OPERATIONAL 
        }
        MachineTab.Out_of_Order -> allMachines.filter { 
            it.statusType == MachineStatusType.MARK_AS_OUT_OF_ORDER || 
            it.statusType == MachineStatusType.OFFLINE 
        }
    }
    
    val tabs = listOf(
        MachineTabItem(MachineTab.Active_Machines, "Active Machines", R.drawable.ic_tripod, selectedTab == MachineTab.Active_Machines),
        MachineTabItem(MachineTab.Under_Maintenance, "Under Maintenance", R.drawable.ic_assigned_time, selectedTab == MachineTab.Under_Maintenance),
        MachineTabItem(MachineTab.Available, "Available", R.drawable.ic_vector, selectedTab == MachineTab.Available),
        MachineTabItem(MachineTab.Out_of_Order, "Out of Order", R.drawable.ic_ticket_thread, selectedTab == MachineTab.Out_of_Order)
    )

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Machine Status",
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
            MachineTabSelector(
                tabs = tabs,
                onTabSelected = { selectedTab = it }
            )
            
            Spacer(modifier = Modifier.height(ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp)))
            
            // Machine Grid
            LazyVerticalGrid(
                columns = ResponsiveLayout.getGridColumns(),
                contentPadding = ResponsiveLayout.getContentPadding(),
                verticalArrangement = ResponsiveLayout.getVerticalGridArrangement(),
                horizontalArrangement = ResponsiveLayout.getGridArrangement()
            ) {
                items(filteredMachines) { machine ->
                    MachineGridCard(
                        machine = machine,
                        onClick = {
                            navController.navigate(Screen.MachineDetailScreen.createRoute(machine.id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MachineTabSelector(
    tabs: List<MachineTabItem>,
    onTabSelected: (MachineTab) -> Unit
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
fun MachineGridCard(
    machine: MachineData,
    onClick: () -> Unit,
    shape: Shape = RectangleShape,
    imageHeight: Dp = ResponsiveLayout.getResponsiveSize(125.dp, 140.dp, 160.dp),
    detailHeight: Dp = ResponsiveLayout.getResponsiveSize(75.dp, 85.dp, 95.dp)
) {
    Card(
        modifier = Modifier,
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
                    model = machine.imageUrl,
                    contentDescription = "Machine Image",
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
                    .padding(top = ResponsiveLayout.getResponsiveSize(6.dp, 8.dp, 10.dp)),
                horizontalAlignment = Alignment.Start
            ) {
                CustomLabel(
                    header = machine.name,
                    headerColor = onSurfaceColor,
                    fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                    modifier = Modifier.padding(
                        horizontal = ResponsiveLayout.getHorizontalPadding(),
                        vertical = ResponsiveLayout.getResponsiveSize(2.dp, 3.dp, 4.dp)
                    )
                )

                // Status Badge
                Box(
                    modifier = Modifier
                        .padding(
                            horizontal = ResponsiveLayout.getHorizontalPadding(),
                            vertical = ResponsiveLayout.getResponsiveSize(3.dp, 4.dp, 5.dp)
                        )
                ) {
                    CustomLabel(
                        header = machine.statusType.label,
                        headerColor = whiteColor,
                        fontSize = ResponsiveLayout.getResponsiveFontSize(10.sp, 12.sp, 14.sp),
                        modifier = Modifier
                            .background(
                                color = machine.statusType.color,
                                shape = RoundedCornerShape(pxToDp(12))
                            )
                            .padding(
                                horizontal = pxToDp(8),
                                vertical = pxToDp(4)
                            )
                    )
                }

                CustomLabel(
                    header = machine.facilityName,
                    headerColor = lightTextColor,
                    fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                    modifier = Modifier.padding(
                        horizontal = ResponsiveLayout.getHorizontalPadding(),
                        vertical = ResponsiveLayout.getResponsiveSize(3.dp, 4.dp, 5.dp)
                    )
                )

                Row(
                    modifier = Modifier.padding(
                        horizontal = ResponsiveLayout.getHorizontalPadding(),
                        vertical = ResponsiveLayout.getResponsiveSize(3.dp, 4.dp, 5.dp)
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
                        header = machine.location,
                        headerColor = lightTextColor,
                        fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                        modifier = Modifier.padding(bottom = 0.dp)
                    )
                }
            }
        }
    }
}

// Data class for machine
data class MachineData(
    val id: String,
    val name: String,
    val statusType: MachineStatusType,
    val location: String,
    val imageUrl: Int,
    val facilityName: String,
    val lastMaintenance: String,
    val nextMaintenance: String,
    val brand: String,
    val model: String
)

@Preview(showBackground = true)
@Composable
fun MachineStatusScreenPreview() {
    val navController = rememberNavController()
    MachineStatusScreen(navController = navController)
}
