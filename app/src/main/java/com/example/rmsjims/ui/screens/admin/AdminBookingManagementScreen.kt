package com.example.rmsjims.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rmsjims.navigation.Screen
import com.example.rmsjims.ui.components.AppSearchBar
import com.example.rmsjims.ui.components.CustomLabel
import com.example.rmsjims.ui.components.CustomNavigationBar
import com.example.rmsjims.ui.components.CustomTopBar
import com.example.rmsjims.ui.screens.staff.InfoCard
import com.example.rmsjims.ui.screens.staff.TabSelector
import com.example.rmsjims.ui.theme.chipColor
import com.example.rmsjims.ui.theme.onSurfaceColor
import com.example.rmsjims.ui.theme.onSurfaceVariant
import com.example.rmsjims.ui.theme.primaryColor
import com.example.rmsjims.ui.theme.whiteColor
import com.example.rmsjims.util.ResponsiveLayout
import com.example.rmsjims.util.pxToDp
import com.example.rmsjims.viewmodel.BookingScreenViewmodel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AdminBookingManagementScreen(
    navController: NavHostController,
    viewModel: BookingScreenViewmodel = koinViewModel()
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }

    androidx.compose.material.Scaffold(
        topBar = {
            CustomTopBar(title = "Booking Management")
        },
        bottomBar = {
            CustomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    PaddingValues(
                        start = ResponsiveLayout.getHorizontalPadding(),
                        end = ResponsiveLayout.getHorizontalPadding(),
                        top = ResponsiveLayout.getVerticalPadding(),
                        bottom = ResponsiveLayout.getVerticalPadding()
                    )
                )
                .padding(paddingValues)
        ) {
            // Search + properties header (from Home style)
            Spacer(modifier = Modifier.height(ResponsiveLayout.getResponsivePadding(12.dp, 16.dp, 20.dp)))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ResponsiveLayout.getCardSpacing()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    modifier = Modifier
                        .height(ResponsiveLayout.getResponsiveSize(46.dp, 60.dp, 68.dp))
                        .weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(ResponsiveLayout.getResponsivePadding(12.dp, 16.dp, 20.dp)))

            AdminBookingFilterSection()

            Spacer(modifier = Modifier.height(ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp)))

            // Tabs + booking list – identical UI to staff BookingScreen
            TabSelector(
                tabs = viewModel.tabs,
                onTabSelected = { selectedTab ->
                    viewModel.onTabSelect(selectedTab)
                }
            )

            Spacer(modifier = Modifier.height(ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp)))

            val selectedTab = viewModel.selectedTab
            val filteredBookings = remember(selectedTab) {
                viewModel.filteredBookings
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp))
            ) {
                items(filteredBookings.size) { index ->
                    val booking = filteredBookings[index]
                    InfoCard(
                        bookingItem = booking,
                        onEditBooking = { navController.navigate(Screen.CalendarScreen.route) },
                        onExtendBooking = { /* Admin extend booking action */ },
                        onReRequestBooking = { /* Admin re-request booking action */ }
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminBookingFilterSection() {
    val mainFilters = listOf(
        "Status",
        "Date",
        "User",
        "Department",
        "Building",
        "Priority"
    )

    var selectedFilter by rememberSaveable { mutableStateOf("Status") }
    var selectedSubProperty by rememberSaveable { mutableStateOf<String?>(null) }
    var propertiesExpanded by remember { mutableStateOf(false) }

    val subProperties = when (selectedFilter) {
        "Status" -> listOf("Pending", "Confirmed", "Cancelled", "Completed", "Overdue")
        "Date" -> listOf("Today", "Tomorrow", "Weekly", "Monthly")
        "User" -> listOf("Assigned by Staff", "Assigned by Assistant")
        "Department" -> listOf(
            "All Departments",
            "Computer Applications",
            "Engineering",
            "Management",
            "Science"
        )
        "Building" -> listOf(
            "All Buildings",
            "Building A",
            "Building B",
            "Building C",
            "Main Campus"
        )
        "Priority" -> listOf("High", "Medium", "Low")
        else -> emptyList()
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getResponsivePadding(12.dp, 16.dp, 20.dp))
    ) {
        // Properties dropdown chip – same visual style as Home properties chip
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.material3.Surface(
                shape = RoundedCornerShape(pxToDp(18)),
                color = primaryColor.copy(alpha = 0.12f),
                border = androidx.compose.foundation.BorderStroke(
                    width = pxToDp(1),
                    color = primaryColor
                ),
                onClick = { propertiesExpanded = true }
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = ResponsiveLayout.getResponsivePadding(14.dp, 16.dp, 18.dp),
                        vertical = ResponsiveLayout.getResponsivePadding(8.dp, 10.dp, 12.dp)
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(pxToDp(8))
                ) {
                    CustomLabel(
                        header = "Properties: $selectedFilter",
                        headerColor = primaryColor,
                        fontSize = ResponsiveLayout.getResponsiveFontSize(13.sp, 14.sp, 15.sp),
                        fontWeight = FontWeight.Medium,
                        maxLine = 1
                    )
                    androidx.compose.material3.Icon(
                        imageVector = androidx.compose.material.icons.Icons.Filled.ArrowDropDown,
                        contentDescription = "Select property",
                        tint = primaryColor
                    )
                }
            }

            DropdownMenu(
                expanded = propertiesExpanded,
                onDismissRequest = { propertiesExpanded = false },
                modifier = Modifier.background(color = whiteColor)
            ) {
                mainFilters.forEach { filter ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = filter,
                                color = if (filter == selectedFilter) primaryColor else onSurfaceColor
                            )
                        },
                        onClick = {
                            selectedFilter = filter
                            selectedSubProperty = null
                            propertiesExpanded = false
                        }
                    )
                }
            }
        }

        // Sub-properties row – horizontal chips similar to Home style
        if (subProperties.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                subProperties.forEach { subProperty ->
                    val selected = selectedSubProperty == subProperty
                    AdminSubPropertyChip(
                        label = subProperty,
                        selected = selected,
                        onClick = {
                            selectedSubProperty = if (selected) null else subProperty
                        }
                    )
                }
            }
        }

        // Lightweight summary of current filter (optional, admin-facing context)
        if (selectedSubProperty != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = onSurfaceVariant),
                shape = RoundedCornerShape(pxToDp(12))
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = ResponsiveLayout.getResponsivePadding(14.dp, 18.dp, 20.dp),
                        vertical = ResponsiveLayout.getResponsivePadding(10.dp, 12.dp, 14.dp)
                    ),
                    verticalArrangement = Arrangement.spacedBy(pxToDp(6))
                ) {
                    CustomLabel(
                        header = "Applied Filter",
                        headerColor = onSurfaceColor,
                        fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 13.sp, 14.sp),
                        fontWeight = FontWeight.SemiBold
                    )
                    CustomLabel(
                        header = "$selectedFilter • $selectedSubProperty",
                        headerColor = onSurfaceColor.copy(alpha = 0.8f),
                        fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 13.sp, 14.sp)
                    )
                    Divider(
                        modifier = Modifier.padding(top = pxToDp(4)),
                        color = onSurfaceColor.copy(alpha = 0.08f),
                        thickness = pxToDp(1)
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminSubPropertyChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) primaryColor.copy(alpha = 0.12f) else Color.Transparent
    val borderColor = if (selected) primaryColor else chipColor

    androidx.compose.material3.Surface(
        shape = RoundedCornerShape(pxToDp(18)),
        color = backgroundColor,
        border = androidx.compose.foundation.BorderStroke(width = pxToDp(1), color = borderColor),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = ResponsiveLayout.getResponsivePadding(14.dp, 16.dp, 18.dp),
                vertical = ResponsiveLayout.getResponsivePadding(6.dp, 8.dp, 10.dp)
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(pxToDp(6))
        ) {
            Text(
                text = label,
                color = if (selected) primaryColor else onSurfaceColor,
                fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 13.sp, 14.sp),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminBookingManagementScreenPreview() {
    val navController = rememberNavController()
    AdminBookingManagementScreen(navController = navController)
}


