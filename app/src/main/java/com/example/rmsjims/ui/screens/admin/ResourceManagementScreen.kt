package com.example.rmsjims.ui.screens.admin

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rmsjims.ui.components.AppButton
import com.example.rmsjims.ui.components.CustomLabel
import com.example.rmsjims.ui.components.CustomNavigationBar
import com.example.rmsjims.ui.components.CustomTopBar
import com.example.rmsjims.ui.theme.cardColor
import com.example.rmsjims.ui.theme.onSurfaceColor
import com.example.rmsjims.ui.theme.onSurfaceVariant
import com.example.rmsjims.ui.theme.primaryColor
import com.example.rmsjims.ui.theme.whiteColor
import com.example.rmsjims.util.ResponsiveLayout
import com.example.rmsjims.util.pxToDp

@Composable
fun ResourceManagementScreen(
    navController: NavHostController
) {
    // --- Usage & Trends (placeholder UI state only) ---
    val topUsedEquipments = listOf(
        UsageSummary("3D Printer A", 128),
        UsageSummary("Microscope X2", 96),
        UsageSummary("CNC Mill 01", 82),
        UsageSummary("Oscilloscope Pro", 74),
        UsageSummary("Laser Cutter L1", 63)
    )
    val leastUsedEquipments = listOf(
        UsageSummary("Spare Projector", 4),
        UsageSummary("Archive Scanner", 6),
        UsageSummary("Storage Rack B", 9),
        UsageSummary("Label Printer", 11),
        UsageSummary("Old Workstation", 13)
    )
    val departmentTopEquipments = listOf(
        DepartmentUsage("Mechanical", "CNC Mill 01", 82),
        DepartmentUsage("Electronics", "Oscilloscope Pro", 74),
        DepartmentUsage("Biotech", "Microscope X2", 96),
        DepartmentUsage("Design", "3D Printer A", 128),
        DepartmentUsage("Research", "Laser Cutter L1", 63)
    )

    // --- Maintenance scheduling (placeholder) ---
    val maintenanceItems = listOf(
        MaintenanceItem(
            equipmentName = "3D Printer A",
            nextSchedule = "Every 2 weeks",
            maintenanceCost = "₹ 4,500",
            servicePriceHistory = "₹ 3,800 → ₹ 4,500",
            warrantyInfo = "Warranty till Dec 2026",
            lastLog = "Nozzle replaced - 12 Aug",
            cycle = "Custom",
            technician = "Ankit Verma",
            raisedBy = "Lab Assistant • Mech Lab",
            issueDescription = "Layer shifting on long prints"
        ),
        MaintenanceItem(
            equipmentName = "Microscope X2",
            nextSchedule = "Monthly",
            maintenanceCost = "₹ 2,100",
            servicePriceHistory = "₹ 2,000 → ₹ 2,100",
            warrantyInfo = "Warranty till Mar 2025",
            lastLog = "Lens cleaned - 02 Sep",
            cycle = "Monthly",
            technician = "Priya Sharma",
            raisedBy = "Staff • Biology Lab",
            issueDescription = "Focus drift over time"
        )
    )

    // --- Cost & financial overview (placeholder) ---
    var showMonthlyOnly by remember { mutableStateOf(true) }
    var showBudgetAlerts by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Operations & Costs",
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

            LazyColumn(
                contentPadding = PaddingValues(
                    horizontal = ResponsiveLayout.getHorizontalPadding(),
                    vertical = ResponsiveLayout.getVerticalPadding()
                ),
                verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getVerticalPadding())
            ) {
                item {
                    UsageAndTrendAnalyticsSection(
                        topUsedEquipments = topUsedEquipments,
                        leastUsedEquipments = leastUsedEquipments,
                        departmentTopEquipments = departmentTopEquipments
                    )
                }

                item {
                    EquipmentConditionOverviewSection()
                }

                item {
                    MaintenanceSchedulingSection(
                        maintenanceItems = maintenanceItems
                    )
                }

                item {
                    CostAndFinancialOverviewSection(
                        showMonthlyOnly = showMonthlyOnly,
                        onShowMonthlyOnlyChange = { showMonthlyOnly = it },
                        showBudgetAlerts = showBudgetAlerts,
                        onShowBudgetAlertsChange = { showBudgetAlerts = it }
                    )
                }

                item {
                    ExportAndReportsSection()
                }
            }
        }
    }
}

@Composable
private fun UsageAndTrendAnalyticsSection(
    topUsedEquipments: List<UsageSummary>,
    leastUsedEquipments: List<UsageSummary>,
    departmentTopEquipments: List<DepartmentUsage>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = onSurfaceVariant),
        shape = RoundedCornerShape(ResponsiveLayout.getResponsiveSize(14.dp, 18.dp, 22.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp),
                    vertical = ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp)
                ),
            verticalArrangement = Arrangement.spacedBy(pxToDp(12))
        ) {
            CustomLabel(
                header = "Usage & Trend Analytics",
                fontSize = ResponsiveLayout.getResponsiveFontSize(18.sp, 20.sp, 22.sp),
                headerColor = onSurfaceColor,
                fontWeight = FontWeight.SemiBold
            )
            CustomLabel(
                header = "Visual overview of total usage hours and department-wise equipment demand.",
                fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                headerColor = onSurfaceColor.copy(0.7f)
            )

            Spacer(modifier = Modifier.height(pxToDp(8)))

            // Top used equipments – simple horizontal usage bars
            CustomLabel(
                header = "Top 5 most used equipment",
                fontSize = ResponsiveLayout.getResponsiveFontSize(13.sp, 15.sp, 17.sp),
                headerColor = onSurfaceColor,
                fontWeight = FontWeight.Medium
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(pxToDp(6))
            ) {
                val maxHours = (topUsedEquipments.maxOfOrNull { it.hours } ?: 1).toFloat()
                topUsedEquipments.forEach { item ->
                    UsageBarRow(
                        label = item.name,
                        valueLabel = "${item.hours} hrs",
                        fraction = item.hours / maxHours
                    )
                }
            }

            Divider(
                modifier = Modifier.padding(vertical = pxToDp(8)),
                thickness = pxToDp(1),
                color = cardColor
            )

            // Least used equipments
            CustomLabel(
                header = "Top 5 least used equipment",
                fontSize = ResponsiveLayout.getResponsiveFontSize(13.sp, 15.sp, 17.sp),
                headerColor = onSurfaceColor,
                fontWeight = FontWeight.Medium
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(pxToDp(6))
            ) {
                val maxHours = (leastUsedEquipments.maxOfOrNull { it.hours } ?: 1).toFloat()
                leastUsedEquipments.forEach { item ->
                    UsageBarRow(
                        label = item.name,
                        valueLabel = "${item.hours} hrs",
                        fraction = item.hours / maxHours
                    )
                }
            }

            Divider(
                modifier = Modifier.padding(vertical = pxToDp(8)),
                thickness = pxToDp(1),
                color = cardColor
            )

            // Department-wise one top equipment each
            CustomLabel(
                header = "Department-wise top used equipment",
                fontSize = ResponsiveLayout.getResponsiveFontSize(13.sp, 15.sp, 17.sp),
                headerColor = onSurfaceColor,
                fontWeight = FontWeight.Medium
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(pxToDp(6))
            ) {
                departmentTopEquipments.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(pxToDp(2))
                        ) {
                            CustomLabel(
                                header = item.department,
                                fontSize = ResponsiveLayout.getResponsiveFontSize(13.sp, 15.sp, 17.sp),
                                headerColor = onSurfaceColor
                            )
                            CustomLabel(
                                header = item.equipment,
                                fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                                headerColor = onSurfaceColor.copy(0.7f)
                            )
                        }
                        CustomLabel(
                            header = "${item.hours} hrs",
                            fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                            headerColor = primaryColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UsageBarRow(
    label: String,
    valueLabel: String,
    fraction: Float
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(pxToDp(4))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomLabel(
                header = label,
                fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                headerColor = onSurfaceColor
            )
            CustomLabel(
                header = valueLabel,
                fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                headerColor = onSurfaceColor.copy(0.8f)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(pxToDp(6))
                .clip(RoundedCornerShape(pxToDp(999)))
                .background(color = cardColor.copy(alpha = 0.6f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction.coerceIn(0.1f, 1f))
                    .height(pxToDp(6))
                    .clip(RoundedCornerShape(pxToDp(999)))
                    .background(color = primaryColor.copy(alpha = 0.9f))
            )
        }
    }
}

@Composable
private fun EquipmentConditionOverviewSection() {
    val conditionCards = listOf(
        ConditionCardData(
            title = "Good Condition",
            count = "148",
            accentColor = Color(0xFF1ABC9C)
        ),
        ConditionCardData(
            title = "Under Maintenance",
            count = "23",
            accentColor = Color(0xFFF39C12)
        ),
        ConditionCardData(
            title = "Offline",
            count = "9",
            accentColor = Color(0xFF7F8C8D)
        ),
        ConditionCardData(
            title = "Needs Repair",
            count = "6",
            accentColor = Color(0xFFE74C3C)
        )
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = onSurfaceVariant),
        shape = RoundedCornerShape(ResponsiveLayout.getResponsiveSize(14.dp, 18.dp, 22.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp),
                    vertical = ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp)
                ),
            verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getResponsivePadding(10.dp, 12.dp, 14.dp))
        ) {
            CustomLabel(
                header = "Equipment Condition Overview",
                fontSize = ResponsiveLayout.getResponsiveFontSize(18.sp, 20.sp, 22.sp),
                headerColor = onSurfaceColor,
                fontWeight = FontWeight.SemiBold
            )
            CustomLabel(
                header = "Quick snapshot of health across all equipment.",
                fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                headerColor = onSurfaceColor.copy(0.7f)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getResponsivePadding(10.dp, 12.dp, 14.dp))
            ) {
                // Two cards per row, compact but not identical to EquipmentManagement cards
                conditionCards.chunked(2).forEach { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(ResponsiveLayout.getCardSpacing())
                    ) {
                        pair.forEach { data ->
                            ConditionCard(
                                modifier = Modifier.weight(1f),
                                data = data
                            )
                        }
                        if (pair.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ConditionCard(
    modifier: Modifier = Modifier,
    data: ConditionCardData
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = whiteColor),
        shape = RoundedCornerShape(ResponsiveLayout.getResponsiveSize(10.dp, 12.dp, 14.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = pxToDp(2))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ResponsiveLayout.getResponsivePadding(12.dp, 14.dp, 16.dp)),
            verticalArrangement = Arrangement.spacedBy(pxToDp(6))
        ) {
            Box(
                modifier = Modifier
                    .height(pxToDp(4))
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(pxToDp(999)))
                    .background(color = data.accentColor.copy(alpha = 0.9f))
            )
            CustomLabel(
                header = data.title,
                fontSize = ResponsiveLayout.getResponsiveFontSize(13.sp, 15.sp, 17.sp),
                headerColor = onSurfaceColor
            )
            CustomLabel(
                header = "${data.count} items",
                fontSize = ResponsiveLayout.getResponsiveFontSize(18.sp, 20.sp, 22.sp),
                headerColor = data.accentColor,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun MaintenanceSchedulingSection(
    maintenanceItems: List<MaintenanceItem>
) {
    var enableBulkSelection by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = onSurfaceVariant),
        shape = RoundedCornerShape(ResponsiveLayout.getResponsiveSize(14.dp, 18.dp, 22.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp),
                    vertical = ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp)
                ),
            verticalArrangement = Arrangement.spacedBy(pxToDp(12))
        ) {
            CustomLabel(
                header = "Maintenance Scheduling",
                fontSize = ResponsiveLayout.getResponsiveFontSize(18.sp, 20.sp, 22.sp),
                headerColor = onSurfaceColor,
                fontWeight = FontWeight.SemiBold
            )
            CustomLabel(
                header = "Plan upcoming maintenance, track logs and manage costs per equipment.",
                fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                headerColor = onSurfaceColor.copy(0.7f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(pxToDp(8))
                ) {
                    Switch(
                        checked = enableBulkSelection,
                        onCheckedChange = { enableBulkSelection = it }
                    )
                    CustomLabel(
                        header = "Bulk maintenance actions",
                        fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                        headerColor = onSurfaceColor
                    )
                }

                AppButton(
                    buttonText = "Schedule new maintenance",
                    onClick = { },
                    modifier = Modifier
                )
            }

            Divider(
                modifier = Modifier.padding(vertical = pxToDp(8)),
                thickness = pxToDp(1),
                color = cardColor
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getResponsivePadding(10.dp, 12.dp, 14.dp))
            ) {
                maintenanceItems.forEach { item ->
                    MaintenanceItemCard(
                        item = item,
                        showBulkSelector = enableBulkSelection
                    )
                }
            }
        }
    }
}

@Composable
private fun MaintenanceItemCard(
    item: MaintenanceItem,
    showBulkSelector: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = whiteColor),
        shape = RoundedCornerShape(ResponsiveLayout.getResponsiveSize(10.dp, 12.dp, 14.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = pxToDp(1))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ResponsiveLayout.getResponsivePadding(12.dp, 14.dp, 16.dp)),
            verticalArrangement = Arrangement.spacedBy(pxToDp(8))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomLabel(
                    header = item.equipmentName,
                    fontSize = ResponsiveLayout.getResponsiveFontSize(14.sp, 16.sp, 18.sp),
                    headerColor = onSurfaceColor,
                    fontWeight = FontWeight.SemiBold
                )

                if (showBulkSelector) {
                    // Simple switch for bulk selection (placeholder)
                    Switch(
                        checked = false,
                        onCheckedChange = { }
                    )
                }
            }

            CustomLabel(
                header = "Next schedule: ${item.nextSchedule}",
                fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                headerColor = onSurfaceColor.copy(0.8f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(pxToDp(4))
                ) {
                    CustomLabel(
                        header = "Maintenance cost",
                        fontSize = ResponsiveLayout.getResponsiveFontSize(11.sp, 13.sp, 15.sp),
                        headerColor = onSurfaceColor.copy(0.7f)
                    )
                    CustomLabel(
                        header = item.maintenanceCost,
                        fontSize = ResponsiveLayout.getResponsiveFontSize(13.sp, 15.sp, 17.sp),
                        headerColor = primaryColor,
                        fontWeight = FontWeight.Medium
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(pxToDp(4))
                ) {
                    CustomLabel(
                        header = "Service price history",
                        fontSize = ResponsiveLayout.getResponsiveFontSize(11.sp, 13.sp, 15.sp),
                        headerColor = onSurfaceColor.copy(0.7f)
                    )
                    CustomLabel(
                        header = item.servicePriceHistory,
                        fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                        headerColor = onSurfaceColor.copy(0.9f)
                    )
                }
            }

            Divider(thickness = pxToDp(1), color = cardColor.copy(alpha = 0.7f))

            Column(
                verticalArrangement = Arrangement.spacedBy(pxToDp(4))
            ) {
                InfoLabelRow(label = "Warranty", value = item.warrantyInfo)
                InfoLabelRow(label = "Last maintenance log", value = item.lastLog)
                InfoLabelRow(label = "Maintenance cycle", value = item.cycle)
                InfoLabelRow(label = "Technician", value = item.technician)
                InfoLabelRow(label = "Raised by", value = item.raisedBy)
                InfoLabelRow(label = "Issue description", value = item.issueDescription)
            }
        }
    }
}

@Composable
private fun InfoLabelRow(
    label: String,
    value: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(pxToDp(2))
    ) {
        CustomLabel(
            header = label,
            fontSize = ResponsiveLayout.getResponsiveFontSize(11.sp, 13.sp, 15.sp),
            headerColor = onSurfaceColor.copy(0.7f)
        )
        CustomLabel(
            header = value,
            fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
            headerColor = onSurfaceColor
        )
    }
}

@Composable
private fun CostAndFinancialOverviewSection(
    showMonthlyOnly: Boolean,
    onShowMonthlyOnlyChange: (Boolean) -> Unit,
    showBudgetAlerts: Boolean,
    onShowBudgetAlertsChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = onSurfaceVariant),
        shape = RoundedCornerShape(ResponsiveLayout.getResponsiveSize(14.dp, 18.dp, 22.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp),
                    vertical = ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp)
                ),
            verticalArrangement = Arrangement.spacedBy(pxToDp(12))
        ) {
            CustomLabel(
                header = "Cost & Financial Overview",
                fontSize = ResponsiveLayout.getResponsiveFontSize(18.sp, 20.sp, 22.sp),
                headerColor = onSurfaceColor,
                fontWeight = FontWeight.SemiBold
            )
            CustomLabel(
                header = "Monitor monthly costs, budgets and vendor reliability.",
                fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                headerColor = onSurfaceColor.copy(0.7f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(pxToDp(4))
                ) {
                    CustomLabel(
                        header = "Total cost - current month",
                        fontSize = ResponsiveLayout.getResponsiveFontSize(11.sp, 13.sp, 15.sp),
                        headerColor = onSurfaceColor.copy(0.7f)
                    )
                    CustomLabel(
                        header = "₹ 2,86,400",
                        fontSize = ResponsiveLayout.getResponsiveFontSize(20.sp, 22.sp, 24.sp),
                        headerColor = primaryColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(pxToDp(4))
                ) {
                    CustomLabel(
                        header = "Filter to monthly view",
                        fontSize = ResponsiveLayout.getResponsiveFontSize(11.sp, 13.sp, 15.sp),
                        headerColor = onSurfaceColor.copy(0.7f)
                    )
                    Switch(
                        checked = showMonthlyOnly,
                        onCheckedChange = onShowMonthlyOnlyChange
                    )
                }
            }

            Divider(thickness = pxToDp(1), color = cardColor)

            // Cost by category
            CustomLabel(
                header = "Cost by category",
                fontSize = ResponsiveLayout.getResponsiveFontSize(13.sp, 15.sp, 17.sp),
                headerColor = onSurfaceColor,
                fontWeight = FontWeight.Medium
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(pxToDp(4))
            ) {
                InfoLabelRow(label = "Equipment purchases", value = "₹ 1,45,000")
                InfoLabelRow(label = "Maintenance", value = "₹ 72,000")
                InfoLabelRow(label = "Repairs", value = "₹ 41,500")
                InfoLabelRow(label = "Consumables", value = "₹ 27,900")
            }

            Divider(thickness = pxToDp(1), color = cardColor)

            // Budget management
            CustomLabel(
                header = "Budget management",
                fontSize = ResponsiveLayout.getResponsiveFontSize(13.sp, 15.sp, 17.sp),
                headerColor = onSurfaceColor,
                fontWeight = FontWeight.Medium
            )

            BudgetRow(label = "Maintenance budget", used = 72, totalLabel = "₹ 1,00,000")
            BudgetRow(label = "Equipment budget", used = 58, totalLabel = "₹ 2,50,000")
            BudgetRow(label = "Consumables budget", used = 66, totalLabel = "₹ 80,000")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomLabel(
                    header = "Show alerts when limit exceeded",
                    fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                    headerColor = onSurfaceColor
                )
                Switch(
                    checked = showBudgetAlerts,
                    onCheckedChange = onShowBudgetAlertsChange
                )
            }

            Divider(thickness = pxToDp(1), color = cardColor)

            // Vendor information + invoices summary
            CustomLabel(
                header = "Vendors & invoices",
                fontSize = ResponsiveLayout.getResponsiveFontSize(13.sp, 15.sp, 17.sp),
                headerColor = onSurfaceColor,
                fontWeight = FontWeight.Medium
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(pxToDp(6))
            ) {
                VendorRow(
                    vendor = "TechLabs Pvt. Ltd.",
                    contact = "+91 98XXXX2211",
                    reliability = "High • 4.7/5 rating"
                )
                VendorRow(
                    vendor = "BioEquip Co.",
                    contact = "+91 79XXXX8810",
                    reliability = "Medium • 4.1/5 rating"
                )
                VendorRow(
                    vendor = "Campus Services",
                    contact = "+91 88XXXX0034",
                    reliability = "High • Internal vendor"
                )
            }

            Divider(thickness = pxToDp(1), color = cardColor)

            CustomLabel(
                header = "Maintenance invoices",
                fontSize = ResponsiveLayout.getResponsiveFontSize(13.sp, 15.sp, 17.sp),
                headerColor = onSurfaceColor,
                fontWeight = FontWeight.Medium
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(pxToDp(6))
            ) {
                InvoiceRow(
                    invoiceId = "INV-2025-0821",
                    amount = "₹ 18,500",
                    status = "Paid",
                    hasAttachment = true
                )
                InvoiceRow(
                    invoiceId = "INV-2025-0834",
                    amount = "₹ 32,000",
                    status = "Unpaid",
                    hasAttachment = true
                )
                InvoiceRow(
                    invoiceId = "INV-2025-0840",
                    amount = "₹ 12,900",
                    status = "Paid",
                    hasAttachment = false
                )
            }
        }
    }
}

@Composable
private fun BudgetRow(
    label: String,
    used: Int,
    totalLabel: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(pxToDp(4))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CustomLabel(
                header = label,
                fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                headerColor = onSurfaceColor
            )
            CustomLabel(
                header = "$used% of $totalLabel",
                fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                headerColor = onSurfaceColor.copy(0.8f)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(pxToDp(6))
                .clip(RoundedCornerShape(pxToDp(999)))
                .background(color = cardColor.copy(alpha = 0.6f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth((used / 100f).coerceIn(0f, 1f))
                    .height(pxToDp(6))
                    .clip(RoundedCornerShape(pxToDp(999)))
                    .background(color = primaryColor.copy(alpha = 0.9f))
            )
        }
    }
}

@Composable
private fun VendorRow(
    vendor: String,
    contact: String,
    reliability: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(pxToDp(2))
    ) {
        CustomLabel(
            header = vendor,
            fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
            headerColor = onSurfaceColor
        )
        CustomLabel(
            header = contact,
            fontSize = ResponsiveLayout.getResponsiveFontSize(11.sp, 13.sp, 15.sp),
            headerColor = onSurfaceColor.copy(0.8f)
        )
        CustomLabel(
            header = reliability,
            fontSize = ResponsiveLayout.getResponsiveFontSize(11.sp, 13.sp, 15.sp),
            headerColor = primaryColor
        )
    }
}

@Composable
private fun InvoiceRow(
    invoiceId: String,
    amount: String,
    status: String,
    hasAttachment: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(pxToDp(2))
        ) {
            CustomLabel(
                header = invoiceId,
                fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                headerColor = onSurfaceColor
            )
            CustomLabel(
                header = amount,
                fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                headerColor = primaryColor,
                fontWeight = FontWeight.Medium
            )
            CustomLabel(
                header = if (hasAttachment) "Includes scanned invoice / image" else "No attachment uploaded yet",
                fontSize = ResponsiveLayout.getResponsiveFontSize(11.sp, 13.sp, 15.sp),
                headerColor = onSurfaceColor.copy(0.7f)
            )
        }
        CustomLabel(
            header = status,
            fontSize = ResponsiveLayout.getResponsiveFontSize(11.sp, 13.sp, 15.sp),
            headerColor = if (status == "Paid") Color(0xFF27AE60) else Color(0xFFE67E22),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ExportAndReportsSection() {
    var includeCostData by remember { mutableStateOf(true) }
    var includeUsageData by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = onSurfaceVariant),
        shape = RoundedCornerShape(ResponsiveLayout.getResponsiveSize(14.dp, 18.dp, 22.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp),
                    vertical = ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp)
                ),
            verticalArrangement = Arrangement.spacedBy(pxToDp(12))
        ) {
            CustomLabel(
                header = "Exporting & Reports",
                fontSize = ResponsiveLayout.getResponsiveFontSize(18.sp, 20.sp, 22.sp),
                headerColor = onSurfaceColor,
                fontWeight = FontWeight.SemiBold
            )
            CustomLabel(
                header = "Export operational and cost data as CSV or PDF for audits and reporting.",
                fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                headerColor = onSurfaceColor.copy(0.7f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomLabel(
                    header = "Include cost & invoice data",
                    fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                    headerColor = onSurfaceColor
                )
                Switch(
                    checked = includeCostData,
                    onCheckedChange = { includeCostData = it }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomLabel(
                    header = "Include usage & trend analytics",
                    fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                    headerColor = onSurfaceColor
                )
                Switch(
                    checked = includeUsageData,
                    onCheckedChange = { includeUsageData = it }
                )
            }

            Divider(thickness = pxToDp(1), color = cardColor)

            Column(
                verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getResponsivePadding(10.dp, 12.dp, 14.dp))
            ) {
                AppButton(
                    buttonText = "Export selected equipment – CSV",
                    onClick = { },
                    modifier = Modifier.fillMaxWidth()
                )
                AppButton(
                    buttonText = "Export selected equipment – PDF",
                    onClick = { },
                    modifier = Modifier.fillMaxWidth()
                )
                AppButton(
                    buttonText = "Bulk export (all filtered items)",
                    onClick = { },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// --- Placeholder models for UI only ---

data class UsageSummary(
    val name: String,
    val hours: Int
)

data class DepartmentUsage(
    val department: String,
    val equipment: String,
    val hours: Int
)

data class ConditionCardData(
    val title: String,
    val count: String,
    val accentColor: Color
)

data class MaintenanceItem(
    val equipmentName: String,
    val nextSchedule: String,
    val maintenanceCost: String,
    val servicePriceHistory: String,
    val warrantyInfo: String,
    val lastLog: String,
    val cycle: String,
    val technician: String,
    val raisedBy: String,
    val issueDescription: String
)

@Preview(showBackground = true)
@Composable
fun ResourceManagementScreenPreview() {
    val navController = rememberNavController()
    ResourceManagementScreen(navController = navController)
}


