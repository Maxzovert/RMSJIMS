package com.example.rmsjims.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.example.rmsjims.ui.components.AppCircularIcon
import com.example.rmsjims.ui.components.AppSearchBar
import com.example.rmsjims.ui.components.CustomLabel
import com.example.rmsjims.ui.components.CustomNavigationBar
import com.example.rmsjims.ui.components.CustomTopBar
import com.example.rmsjims.ui.theme.chipColor
import com.example.rmsjims.ui.theme.categoryIconColor
import com.example.rmsjims.ui.theme.onSurfaceColor
import com.example.rmsjims.ui.theme.onSurfaceVariant
import com.example.rmsjims.ui.theme.primaryColor
import com.example.rmsjims.ui.theme.whiteColor
import com.example.rmsjims.util.ResponsiveLayout
import com.example.rmsjims.util.pxToDp

@Composable
fun EquipmentManagementScreen(
    navController: NavHostController
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    
    // Dynamic two-level filter system - same as Home screen
    val mainPropertyCategories = remember {
        listOf(
            "Status",
            "Category",
            "Building",
            "Location",
            "Assigned To",
            "Maintenance",
            "Age",
            "Priority",
            "Vendor Info"
        )
    }
    var selectedMainProperty by rememberSaveable { mutableStateOf("Status") }
    var selectedSubProperty by rememberSaveable { mutableStateOf<String?>(null) }
    
    // Sub-properties for each main property
    val subProperties = remember(selectedMainProperty) {
        when (selectedMainProperty) {
            "Status" -> listOf("Available", "In Use", "Under Maintenance", "Offline", "Needs Repair")
            "Category" -> listOf("Computers", "Lab Equipment", "Tools", "Networking Devices", "Cameras", "Furniture")
            "Building" -> listOf("Building A", "Building B", "Building C", "Building D", "Building E", "Building F", "Main Campus", "North Wing")
            "Location" -> listOf("Floor", "Lab", "Room", "Cabinet", "Shelf")
            "Assigned To" -> listOf("Staff", "Assistant", "Not Assigned")
            "Maintenance" -> listOf("Today", "This Week", "This Month", "Overdue", "Recently Serviced")
            "Age" -> listOf("<1 year", "1–3 years", "3–5 years", ">5 years")
            "Priority" -> listOf("High", "Medium", "Low")
            "Vendor Info" -> listOf("Vendor name", "Warranty expiring soon")
            else -> emptyList()
        }
    }
    
    val savedFilters = remember { mutableStateListOf<FilterTag>() }
    
    // Helper function to get count for a sub-property (placeholder - can be connected to actual data)
    val getSubPropertyCount: (String, String) -> Int = { _, _ -> 0 }
    
    Scaffold(
        topBar = {
            CustomTopBar(title = "Equipment Management")
        },
        bottomBar = {
            CustomNavigationBar(navController = navController)
        },
        containerColor = whiteColor
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                start = ResponsiveLayout.getHorizontalPadding(),
                end = ResponsiveLayout.getHorizontalPadding(),
                top = ResponsiveLayout.getVerticalPadding(),
                bottom = ResponsiveLayout.getVerticalPadding()
            ),
            verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getResponsivePadding(18.dp, 24.dp, 28.dp))
        ) {
            // Home screen's original search bar and filter bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(ResponsiveLayout.getCardSpacing())
                ) {
                    AppSearchBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        modifier = Modifier
                            .height(ResponsiveLayout.getResponsiveSize(46.dp, 60.dp, 68.dp))
                            .weight(1f)
                    )

                    AppCircularIcon(
                        onClick = { /* Filter sheet can be added if needed */ }
                    )
                }
            }

            // Two dropdown filter system - same as Home screen
            item {
                TwoDropdownFilterSection(
                    mainProperties = mainPropertyCategories,
                    selectedMainProperty = selectedMainProperty,
                    onMainPropertySelected = { 
                        selectedMainProperty = it
                        selectedSubProperty = null // Reset sub-property when main property changes
                    },
                    subProperties = subProperties,
                    selectedSubProperty = selectedSubProperty,
                    onSubPropertySelected = { selectedSubProperty = it },
                    savedFilters = savedFilters,
                    onRemoveSavedFilter = { filter -> savedFilters.remove(filter) },
                    getSubPropertyCount = getSubPropertyCount,
                    selectedMainPropertyForCount = selectedMainProperty
                )
            }
            
            // Divider between properties section and list
            item {
                Divider(
                    modifier = Modifier.padding(vertical = ResponsiveLayout.getResponsivePadding(8.dp, 10.dp, 12.dp)),
                    color = onSurfaceColor.copy(alpha = 0.1f),
                    thickness = pxToDp(1)
                )
            }

            // Equipment list - placeholder content, can be replaced with actual equipment data
            item {
                CustomLabel(
                    header = "Equipment List",
                    fontSize = ResponsiveLayout.getResponsiveFontSize(18.sp, 20.sp, 22.sp),
                    headerColor = onSurfaceColor,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            // Placeholder equipment cards - replace with actual equipment data
            items(5) { index ->
                EquipmentManagementCard(
                    equipmentName = "Equipment ${index + 1}",
                    status = "Available",
                    location = "Building A, Lab 101",
                    equipmentId = "EQ-${index + 1}",
                    onClick = {
                        navController.navigate(Screen.ProductDescriptionScreen.route)
                    }
                )
            }
        }
    }
}

@Composable
private fun TwoDropdownFilterSection(
    mainProperties: List<String>,
    selectedMainProperty: String,
    onMainPropertySelected: (String) -> Unit,
    subProperties: List<String>,
    selectedSubProperty: String?,
    onSubPropertySelected: (String?) -> Unit,
    savedFilters: List<FilterTag>,
    onRemoveSavedFilter: (FilterTag) -> Unit,
    getSubPropertyCount: (String, String) -> Int,
    selectedMainPropertyForCount: String
) {
    var propertiesExpanded by remember { mutableStateOf(false) }
    val chipSpacing = ResponsiveLayout.getResponsivePadding(10.dp, 12.dp, 14.dp)
    
    Column(
        verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getResponsivePadding(12.dp, 16.dp, 20.dp))
    ) {
        // Properties Dropdown Chip
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(chipSpacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                SelectableChip(
                    label = "Properties: $selectedMainProperty",
                    selected = true,
                    onClick = { propertiesExpanded = true },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Select property",
                            tint = primaryColor
                        )
                    }
                )
                
                DropdownMenu(
                    expanded = propertiesExpanded,
                    onDismissRequest = { propertiesExpanded = false },
                    modifier = Modifier.background(color = whiteColor)
                ) {
                    mainProperties.forEach { property ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = property,
                                    color = if (property == selectedMainProperty) primaryColor else onSurfaceColor
                                )
                            },
                            onClick = {
                                onMainPropertySelected(property)
                                propertiesExpanded = false
                            }
                        )
                    }
                }
            }
        }
        
        // Sub-Properties Tabs (tab-style, like BookingScreen)
        if (subProperties.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                subProperties.forEach { subProperty ->
                    val count = getSubPropertyCount(selectedMainPropertyForCount, subProperty)
                    SubPropertyTab(
                        label = subProperty,
                        count = count,
                        selected = selectedSubProperty == subProperty,
                        onClick = { 
                            onSubPropertySelected(
                                if (selectedSubProperty == subProperty) null else subProperty
                            )
                        }
                    )
                }
            }
        }
        
        // Saved Filters Row
        if (savedFilters.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(chipSpacing),
                verticalAlignment = Alignment.CenterVertically
            ) {
                savedFilters.forEach { filter ->
                    SavedFilterChip(
                        filter = filter,
                        onRemove = onRemoveSavedFilter
                    )
                }
            }
        }
    }
}

@Composable
private fun SavedFilterChip(
    filter: FilterTag,
    onRemove: (FilterTag) -> Unit
) {
    SelectableChip(
        label = "${filter.label} (${filter.count})",
        selected = true,
        onClick = { onRemove(filter) },
        trailingIcon = {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Remove ${filter.label}",
                tint = primaryColor
            )
        }
    )
}

@Composable
private fun SubPropertyTab(
    label: String,
    count: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(pxToDp(10))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(pxToDp(8))
            ) {
                CustomLabel(
                    header = label,
                    fontSize = 12.sp,
                    headerColor = if (selected) primaryColor else categoryIconColor
                )
                // Badge showing count
                if (count > 0) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (selected) primaryColor else categoryIconColor.copy(alpha = 0.2f),
                                shape = CircleShape
                            )
                            .padding(
                                horizontal = ResponsiveLayout.getResponsivePadding(6.dp, 8.dp, 10.dp),
                                vertical = ResponsiveLayout.getResponsivePadding(2.dp, 3.dp, 4.dp)
                            )
                    ) {
                        Text(
                            text = count.toString(),
                            fontSize = ResponsiveLayout.getResponsiveFontSize(10.sp, 11.sp, 12.sp),
                            color = if (selected) whiteColor else categoryIconColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectableChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null
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
                vertical = ResponsiveLayout.getResponsivePadding(8.dp, 10.dp, 12.dp)
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(pxToDp(8))
        ) {
            leadingIcon?.invoke()
            CustomLabel(
                header = label,
                headerColor = if (selected) primaryColor else onSurfaceColor,
                fontSize = ResponsiveLayout.getResponsiveFontSize(13.sp, 14.sp, 15.sp),
                fontWeight = FontWeight.Medium,
                maxLine = 1
            )
            trailingIcon?.invoke()
        }
    }
}

@Composable
private fun EquipmentManagementCard(
    equipmentName: String,
    status: String,
    location: String,
    equipmentId: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = onSurfaceVariant
        ),
        shape = RoundedCornerShape(pxToDp(12))
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp),
                vertical = ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp)
            ),
            verticalArrangement = Arrangement.spacedBy(pxToDp(12))
        ) {
            CustomLabel(
                header = equipmentName,
                fontSize = ResponsiveLayout.getResponsiveFontSize(16.sp, 18.sp, 20.sp),
                headerColor = onSurfaceColor,
                fontWeight = FontWeight.SemiBold
            )
            CustomLabel(
                header = status,
                fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                headerColor = primaryColor
            )
            CustomLabel(
                header = location,
                fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                headerColor = onSurfaceColor.copy(0.7f)
            )
        }
    }
}

data class FilterTag(
    val section: String,
    val label: String,
    val count: Int
)

@Preview(showBackground = true)
@Composable
fun EquipmentManagementScreenPreview() {
    val navController = rememberNavController()
    EquipmentManagementScreen(navController = navController)
}

