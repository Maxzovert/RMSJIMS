package com.example.rmsjims.ui.screens.staff

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import com.example.rmsjims.data.model.PropertyRequest
import com.example.rmsjims.data.model.PropertyRequestGroup
import com.example.rmsjims.data.model.PropertyRequestProvider
import com.example.rmsjims.data.model.UserRole
import com.example.rmsjims.ui.components.AppCircularIcon
import com.example.rmsjims.ui.components.AppFAB
import com.example.rmsjims.ui.components.AppSearchBar
import com.example.rmsjims.ui.components.CustomLabel
import com.example.rmsjims.ui.components.CustomNavigationBar
import com.example.rmsjims.ui.components.CustomSmallLabel
import com.example.rmsjims.ui.screens.assistant.PropertyRequestsContent
import com.example.rmsjims.ui.theme.categoryIconColor
import com.example.rmsjims.ui.theme.chipColor
import com.example.rmsjims.ui.theme.labelColor
import com.example.rmsjims.ui.theme.onSurfaceColor
import com.example.rmsjims.ui.theme.onSurfaceVariant
import com.example.rmsjims.ui.theme.primaryColor
import com.example.rmsjims.ui.theme.titleColor
import com.example.rmsjims.ui.theme.whiteColor
import com.example.rmsjims.util.ResponsiveLayout
import com.example.rmsjims.util.pxToDp
import com.example.rmsjims.viewmodel.FilterSortViewModel
import com.example.rmsjims.viewmodel.SearchViewModel
import com.example.rmsjims.viewmodel.UserSessionViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    filterSortViewModel: FilterSortViewModel = koinViewModel(),
    sessionViewModel: UserSessionViewModel = koinViewModel(),
    searchViewModel: SearchViewModel = koinViewModel()
) {
    val isFilterSheetVisible by filterSortViewModel.isSheetVisible
    val isAiChatSheetVisible by searchViewModel.isChatSheetVisible
    val userRole = sessionViewModel.userRole

    val resources = remember {
        listOf(
            Resource(
                name = "Lab Space A-101",
                type = "Lab Space",
                capacity = "20 students",
                currentUsage = "12 students",
                status = "Available",
                location = "Building A, Floor 1"
            ),
            Resource(
                name = "Conference Room B",
                type = "Meeting Room",
                capacity = "15 people",
                currentUsage = "15 people",
                status = "Fully Booked",
                location = "Building B, Floor 2"
            ),
            Resource(
                name = "Projector System",
                type = "Equipment",
                capacity = "1 unit",
                currentUsage = "In use",
                status = "In Use",
                location = "Lab A-101"
            ),
            Resource(
                name = "Storage Room C",
                type = "Storage",
                capacity = "100 items",
                currentUsage = "65 items",
                status = "Available",
                location = "Building C, Basement"
            ),
            Resource(
                name = "Computer Lab D-201",
                type = "Lab Space",
                capacity = "30 students",
                currentUsage = "25 students",
                status = "In Use",
                location = "Building D, Floor 2"
            ),
            Resource(
                name = "Library Study Room",
                type = "Study Space",
                capacity = "8 people",
                currentUsage = "8 people",
                status = "Fully Booked",
                location = "Building E, Floor 1"
            ),
            Resource(
                name = "Workshop Area",
                type = "Workshop",
                capacity = "15 people",
                currentUsage = "10 people",
                status = "Available",
                location = "Building F, Ground Floor"
            ),
            Resource(
                name = "Seminar Hall",
                type = "Hall",
                capacity = "100 people",
                currentUsage = "0 people",
                status = "Available",
                location = "Building A, Floor 3"
            )
        )
    }

    val savedFilters = remember { mutableStateListOf<FilterTag>() }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    
    // Dynamic two-level filter system
    val mainPropertyCategories = remember {
        listOf(
            "All",
            "Approvals",
            "Department",
            "Building",
            "Resource Type",
            "Status",
            "Urgency",
            "History & Logs",
            "Recently Added",
            "Buildings"
        )
    }
    var selectedMainProperty by rememberSaveable { mutableStateOf("All") }
    var selectedSubProperty by rememberSaveable { mutableStateOf<String?>(null) }
    
    // Sub-properties for each main property
    val subProperties = remember(selectedMainProperty) {
        when (selectedMainProperty) {
            "Department" -> listOf("MCA", "BCA", "Engineering Labs", "Physics", "Chemistry", "Biology", "Mathematics", "Computer Science", "Management Studies", "Sciences")
            "Status" -> listOf("Available", "In Use", "Under Maintenance", "Offline", "Reserved", "Fully Booked")
            "Building" -> listOf("Building A", "Building B", "Building C", "Building D", "Building E", "Building F", "Main Campus", "North Wing")
            "Resource Type" -> listOf("Laboratories", "Equipment", "Spaces", "Meeting Rooms", "Storage", "Workshops", "Study Areas")
            "Urgency" -> listOf("High Priority", "Medium Priority", "Low Priority", "Critical")
            "Approvals" -> listOf("Pending Approvals", "New Requests", "Priority Requests", "Flagged Requests", "Approved Usages", "Rejected Requests")
            "History & Logs" -> listOf("Usage Log", "User Activity", "Machine History", "System Logs")
            "Recently Added" -> listOf("New Machines", "New Users", "New Departments", "New Equipment")
            "Buildings" -> listOf("Building A", "Building B", "Building C", "Building D", "Building E", "Building F", "Main Campus", "North Wing")
            else -> emptyList()
        }
    }

    val propertyRequests = remember { PropertyRequestProvider.sampleRequests() }
    val sections = remember(resources, propertyRequests) { buildResourceSections(resources, propertyRequests) }
    val sectionStates = remember(sections) {
        mutableStateMapOf<String, ResourceSectionState>().apply {
            sections.forEach { section ->
                val initialTab = section.tabs.firstOrNull()
                val initialSub = initialTab?.subTabs?.firstOrNull()
                put(section.title, ResourceSectionState(selectedTab = initialTab, selectedSubTab = initialSub))
            }
        }
    }
    
    // Helper function to get count for a sub-property from sections
    val getSubPropertyCount: (String, String) -> Int = { mainProp, subProp ->
        sections.find { it.title == mainProp }?.tabs?.flatMap { it.subTabs }
            ?.find { it.label == subProp }?.count ?: 0
    }
    
    // Filter sections based on selected property/sub-property
    val filteredSections = remember(selectedMainProperty, selectedSubProperty, sections) {
        if (selectedMainProperty == "All") {
            sections
        } else if (selectedSubProperty != null) {
            // Show only sections that match the selected property and sub-property
            sections.filter { section ->
                section.title == selectedMainProperty && 
                section.tabs.any { tab -> 
                    tab.subTabs.any { it.label == selectedSubProperty }
                }
            }
        } else {
            // Show only sections that match the selected property
            sections.filter { it.title == selectedMainProperty }
        }
    }

    Scaffold(
        bottomBar = {
            CustomNavigationBar(navController = navController)
        },
        floatingActionButton = {
            if (userRole == UserRole.ASSISTANT){
                AppFAB(
                    onClick = {searchViewModel.showChatSheet()},
                    modifier = Modifier
                )
            }
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
            // Title: "Home"
            item {
                CustomLabel(
                    header = "Home",
                    fontSize = ResponsiveLayout.getResponsiveFontSize(20.sp, 24.sp, 28.sp),
                    headerColor = titleColor,
                    fontWeight = FontWeight.SemiBold
                )
            }

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
                    onClick = { filterSortViewModel.showSheet() },
                )
                }
            }

            // Two dropdown filter system
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

            // Show content based on selected property
            if (selectedMainProperty == "All") {
                // Default state: Show only "All Resources" list
                item {
                    SectionDivider(
                        title = "All Resources",
                        badgeCount = resources.size,
                        isHighlighted = searchQuery.isNotEmpty()
                    )
                }

                items(resources) { resource ->
                    ResourceCard(resource = resource)
                }
            } else {
                // When a property is selected: Show only filtered sections with their cards
                filteredSections.forEach { section ->
                    item(key = section.title) {
                        val state = sectionStates[section.title] ?: ResourceSectionState()
                        val selectedTab = state.selectedTab ?: section.tabs.firstOrNull()
                        
                        // If a top-level sub-property is selected, use it to filter content
                        val effectiveSubTab = if (selectedSubProperty != null && selectedMainProperty == section.title) {
                            selectedTab?.subTabs?.find { it.label == selectedSubProperty }
                        } else {
                            state.selectedSubTab
                        }
                        
                        // Show content only for the effective sub-tab (filtered by top-level selection)
                        val propertyRequestItems = effectiveSubTab?.propertyRequests?.takeIf { it.isNotEmpty() }
                        if (propertyRequestItems != null) {
                            PropertyRequestsContent(
                                requests = propertyRequestItems,
                                showPriorityTag = effectiveSubTab.showPriorityTag,
                                showQuickToggle = effectiveSubTab.showQuickToggle,
                                showVerificationAction = effectiveSubTab.showVerificationAction,
                                navController = navController
                            )
                        } else {
                            val contentItems = effectiveSubTab?.content?.takeIf { it.isNotEmpty() }
                                ?: (if (selectedSubProperty == null) selectedTab?.content else emptyList())
                            ResourceSectionContent(
                                items = contentItems.orEmpty()
                            )
                        }
                    }
                }
            }
        }

        if (isFilterSheetVisible) {
            FilterSortBottomSheet(viewModel = filterSortViewModel)
        }

        if (isAiChatSheetVisible){
            ChatBottomSheet(viewModel = searchViewModel)
        }
    }
}

@Composable
fun ResourceCard(resource: Resource) {
    val statusColor = when (resource.status) {
        "Available" -> Color(0xFF26BB64)
        "In Use", "Fully Booked" -> Color(0xFFE67824)
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
            ){
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(pxToDp(4))
                ) {
                    CustomLabel(
                        header = resource.name,
                        fontSize = ResponsiveLayout.getResponsiveFontSize(16.sp, 18.sp, 20.sp),
                        headerColor = onSurfaceColor
                    )
                    CustomLabel(
                        header = resource.type,
                        fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                        headerColor = onSurfaceColor.copy(0.6f)
                    )
                }
                
                Box(
                    modifier = Modifier
                        .background(
                            color = statusColor.copy(0.2f),
                            shape = RoundedCornerShape(pxToDp(12))
                        )
                        .padding(
                            horizontal = pxToDp(12),
                            vertical = pxToDp(6)
                        )
                ) {
                    CustomLabel(
                        header = resource.status,
                        fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                        headerColor = statusColor
                    )
                }
            }
            
            // Details
            Column(
                verticalArrangement = Arrangement.spacedBy(pxToDp(8))
            ) {
                DetailRow("Location", resource.location)
                DetailRow("Capacity", resource.capacity)
                DetailRow("Current Usage", resource.currentUsage)
            }
        }
    }
}

@Composable
private fun ResourceSummaryRow(resources: List<Resource>) {
    val totalResources = resources.size
    val availableResources = resources.count { it.status == "Available" }
    val engagedResources = resources.count { it.status == "In Use" || it.status == "Fully Booked" }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ResponsiveLayout.getCardSpacing())
    ) {
        ResourceSummaryCard(
            label = "Total Resources",
            value = "$totalResources",
            accentColor = primaryColor
        )
        ResourceSummaryCard(
            label = "Available",
            value = "$availableResources",
            accentColor = Color(0xFF26BB64)
        )
        ResourceSummaryCard(
            label = "In Use",
            value = "$engagedResources",
            accentColor = Color(0xFFE67824)
        )
    }
}

@Composable
private fun ResourceSummaryCard(
    label: String,
    value: String,
    accentColor: Color
) {
    Card(
        modifier = Modifier,
        colors = CardDefaults.cardColors(
            containerColor = onSurfaceVariant
        ),
        shape = RoundedCornerShape(pxToDp(12))
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp),
                vertical = ResponsiveLayout.getResponsivePadding(18.dp, 22.dp, 26.dp)
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(pxToDp(6))
        ) {
            CustomLabel(
                header = value,
                headerColor = accentColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = ResponsiveLayout.getResponsiveFontSize(20.sp, 24.sp, 28.sp)
            )
            CustomSmallLabel(
                header = label,
                headerColor = labelColor,
                modifier = Modifier
            )
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
        if (selectedMainProperty != "All" && subProperties.isNotEmpty()) {
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
    Surface(
        shape = RoundedCornerShape(pxToDp(18)),
        color = backgroundColor,
        border = BorderStroke(width = pxToDp(1), color = borderColor),
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
private fun SectionDivider(
    title: String,
    badgeCount: Int,
    isHighlighted: Boolean
) {
    val badgeBackground = if (isHighlighted) primaryColor else onSurfaceColor.copy(alpha = 0.08f)
    val badgeContentColor = if (isHighlighted) whiteColor else onSurfaceColor

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(pxToDp(2)))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
        modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = ResponsiveLayout.getResponsivePadding(6.dp, 8.dp, 10.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(pxToDp(12))
    ) {
        Box(
            modifier = Modifier
                        .weight(1f)
                        .height(pxToDp(1))
                        .background(onSurfaceColor.copy(alpha = 0.1f))
                )
                CustomLabel(
                    header = title,
                    headerColor = onSurfaceColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = ResponsiveLayout.getResponsiveFontSize(16.sp, 18.sp, 20.sp)
                )
                Surface(
                    color = badgeBackground,
                    shape = RoundedCornerShape(pxToDp(12))
                ) {
                    Text(
                        text = badgeCount.toString(),
                        color = badgeContentColor,
                        modifier = Modifier.padding(
                            horizontal = ResponsiveLayout.getResponsivePadding(10.dp, 12.dp, 14.dp),
                            vertical = ResponsiveLayout.getResponsivePadding(4.dp, 6.dp, 8.dp)
                        ),
                        fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 13.sp, 14.sp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SectionTabRow(
    tabs: List<ResourceSectionTab>,
    selectedTab: ResourceSectionTab?,
    onTabSelected: (ResourceSectionTab) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(ResponsiveLayout.getResponsivePadding(12.dp, 14.dp, 16.dp)),
        verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getResponsivePadding(8.dp, 10.dp, 12.dp))
    ) {
        tabs.forEach { tab ->
            val isSelected = selectedTab?.label == tab.label
            SelectableChip(
                label = tab.label,
                selected = isSelected,
                onClick = { onTabSelected(tab) }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SectionSubTabRow(
    subTabs: List<ResourceSectionSubTab>,
    selectedSubTab: ResourceSectionSubTab?,
    onSubTabSelected: (ResourceSectionSubTab) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(ResponsiveLayout.getResponsivePadding(10.dp, 12.dp, 14.dp)),
        verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getResponsivePadding(8.dp, 10.dp, 12.dp))
    ) {
        subTabs.forEach { subTab ->
            val isSelected = selectedSubTab?.label == subTab.label
            SelectableChip(
                label = "${subTab.label} (${subTab.count})",
                selected = isSelected,
                onClick = { onSubTabSelected(subTab) }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ResourceSectionContent(
    items: List<ResourceContentItem>
) {
    if (items.isEmpty()) {
        EmptySectionState()
        return
    }

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(ResponsiveLayout.getCardSpacing()),
        verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getResponsivePadding(12.dp, 14.dp, 16.dp))
    ) {
        items.forEach { item ->
            ResourceInfoCard(item = item)
        }
    }
}

@Composable
private fun EmptySectionState() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = onSurfaceVariant),
        shape = RoundedCornerShape(pxToDp(12))
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = ResponsiveLayout.getResponsivePadding(18.dp, 22.dp, 26.dp),
                vertical = ResponsiveLayout.getResponsivePadding(20.dp, 24.dp, 28.dp)
            ),
            verticalArrangement = Arrangement.spacedBy(pxToDp(6))
        ) {
            CustomLabel(
                header = "Nothing to show yet",
                headerColor = onSurfaceColor,
                fontWeight = FontWeight.SemiBold
            )
            CustomSmallLabel(
                header = "Adjust the filters to view resources or logs for this section.",
                headerColor = labelColor.copy(alpha = 0.8f),
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun ResourceInfoCard(item: ResourceContentItem) {
    Card(
        modifier = Modifier.widthIn(
            min = ResponsiveLayout.getResponsiveSize(180.dp, 220.dp, 260.dp)
        ),
        colors = CardDefaults.cardColors(containerColor = onSurfaceVariant),
        shape = RoundedCornerShape(pxToDp(12))
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = ResponsiveLayout.getResponsivePadding(18.dp, 22.dp, 26.dp),
                vertical = ResponsiveLayout.getResponsivePadding(18.dp, 22.dp, 26.dp)
            ),
            verticalArrangement = Arrangement.spacedBy(pxToDp(6))
        ) {
            CustomLabel(
                header = item.title,
                headerColor = onSurfaceColor,
                fontWeight = FontWeight.SemiBold,
                maxLine = 2
            )
            Text(
                text = item.subtitle,
                color = labelColor,
                fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 13.sp, 14.sp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            CustomSmallLabel(
                header = item.meta,
                headerColor = primaryColor,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
            )
        }
    }
}

private fun MutableList<FilterTag>.updateFilter(section: String, subTab: ResourceSectionSubTab) {
    val tag = FilterTag(section, subTab.label, subTab.count)
    val existingIndex = indexOfFirst { it.section == section }
    if (existingIndex >= 0) {
        this[existingIndex] = tag
    } else {
        add(tag)
    }
}

private fun MutableList<FilterTag>.removeWhere(predicate: (FilterTag) -> Boolean) {
    val iterator = iterator()
    while (iterator.hasNext()) {
        if (predicate(iterator.next())) {
            iterator.remove()
        }
    }
}

private fun buildResourceSections(
    resources: List<Resource>,
    propertyRequests: PropertyRequestGroup
): List<ResourceSection> {
    val recentlyAddedItems = resources.map {
        ResourceContentItem(
            title = it.name,
            subtitle = "${it.type} - ${it.location}",
            meta = "${it.status} - ${it.capacity}"
        )
    }

    return listOf(
        ResourceSection(
            title = "All",
            tabs = listOf(
                ResourceSectionTab(
                    label = "All Resources",
                    count = resources.size,
                    content = recentlyAddedItems.take(4)
                )
            )
        ),
        ResourceSection(
            title = "Approvals",
            tabs = listOf(
                ResourceSectionTab(
                    label = "Pending Approvals",
                    count = propertyRequests.pendingApprovals.size + propertyRequests.newRequests.size + propertyRequests.priorityRequests.size + propertyRequests.flaggedRequests.size,
                    subTabs = listOf(
                        ResourceSectionSubTab(
                            label = "Pending Approvals",
                            count = propertyRequests.pendingApprovals.size,
                            propertyRequests = propertyRequests.pendingApprovals
                        ),
                        ResourceSectionSubTab(
                            label = "New Requests",
                            count = propertyRequests.newRequests.size,
                            propertyRequests = propertyRequests.newRequests
                        ),
                        ResourceSectionSubTab(
                            label = "Priority Requests",
                            count = propertyRequests.priorityRequests.size,
                            propertyRequests = propertyRequests.priorityRequests,
                            showPriorityTag = true,
                            showQuickToggle = true
                        ),
                        ResourceSectionSubTab(
                            label = "Flagged Requests",
                            count = propertyRequests.flaggedRequests.size,
                            propertyRequests = propertyRequests.flaggedRequests,
                            showVerificationAction = true
                        )
                    )
                )
            )
        ),
        ResourceSection(
            title = "Department",
            tabs = listOf(
                ResourceSectionTab(
                    label = "Computer Applications",
                    count = 9,
                    content = listOf(
                        ResourceContentItem(
                            title = "Development Cluster",
                            subtitle = "Full-stack lab with cloud ready nodes",
                            meta = "6 collaborative pods"
                        ),
                        ResourceContentItem(
                            title = "AI & Analytics Lab",
                            subtitle = "High performance systems for ML workloads",
                            meta = "4 GPU rigs available"
                        )
                    ),
                    subTabs = listOf(
                        ResourceSectionSubTab(
                            label = "MCA",
                            count = 4,
                            content = listOf(
                                ResourceContentItem(
                                    title = "Advanced Programming Lab",
                                    subtitle = "Hands-on sessions for distributed systems",
                                    meta = "18/24 seats reserved"
                                )
                            )
                        ),
                        ResourceSectionSubTab(
                            label = "BCA",
                            count = 3,
                            content = listOf(
                                ResourceContentItem(
                                    title = "Design Thinking Studio",
                                    subtitle = "Collaborative space for solution sprints",
                                    meta = "Toolkit refreshed this week"
                                )
                            )
                        )
                    )
                ),
                ResourceSectionTab(
                    label = "Management Studies",
                    count = 6,
                    content = listOf(
                        ResourceContentItem(
                            title = "Strategy Lab",
                            subtitle = "Simulation tools for business cases",
                            meta = "Upcoming module: Market Analytics"
                        )
                    )
                )
            )
        ),
        ResourceSection(
            title = "Building",
            tabs = listOf(
                ResourceSectionTab(
                    label = "Building A",
                    count = 3,
                    content = listOf(
                        ResourceContentItem(
                            title = "Lab Space A-101",
                            subtitle = "Lab Space - Building A, Floor 1",
                            meta = "Available - 20 students"
                        )
                    )
                ),
                ResourceSectionTab(
                    label = "Building B",
                    count = 2,
                    content = listOf(
                        ResourceContentItem(
                            title = "Conference Room B",
                            subtitle = "Meeting Room - Building B, Floor 2",
                            meta = "Fully Booked - 15 people"
                        )
                    )
                ),
                ResourceSectionTab(
                    label = "Building C",
                    count = 1,
                    content = listOf(
                        ResourceContentItem(
                            title = "Storage Room C",
                            subtitle = "Storage - Building C, Basement",
                            meta = "Available - 100 items"
                        )
                    )
                ),
                ResourceSectionTab(
                    label = "Building D",
                    count = 1,
                    content = listOf(
                        ResourceContentItem(
                            title = "Computer Lab D-201",
                            subtitle = "Lab Space - Building D, Floor 2",
                            meta = "In Use - 30 students"
                        )
                    )
                ),
                ResourceSectionTab(
                    label = "Building E",
                    count = 1,
                    content = listOf(
                        ResourceContentItem(
                            title = "Library Study Room",
                            subtitle = "Study Space - Building E, Floor 1",
                            meta = "Fully Booked - 8 people"
                        )
                    )
                ),
                ResourceSectionTab(
                    label = "Building F",
                    count = 1,
                    content = listOf(
                        ResourceContentItem(
                            title = "Workshop Area",
                            subtitle = "Workshop - Building F, Ground Floor",
                            meta = "Available - 15 people"
                        )
                    )
                )
            )
        ),
        ResourceSection(
            title = "Resource Type",
            tabs = listOf(
                ResourceSectionTab(
                    label = "Laboratories",
                    count = 8,
                    content = listOf(
                        ResourceContentItem(
                            title = "Embedded Systems Lab",
                            subtitle = "Microcontroller kits, oscilloscopes, logic analyzers",
                            meta = "5 kits reserved"
                        )
                    )
                ),
                ResourceSectionTab(
                    label = "Equipment",
                    count = 10,
                    content = listOf(
                        ResourceContentItem(
                            title = "3D Printers",
                            subtitle = "FDM and SLA printers with material inventory",
                            meta = "7 jobs queued"
                        )
                    )
                ),
                ResourceSectionTab(
                    label = "Spaces",
                    count = 6,
                    content = listOf(
                        ResourceContentItem(
                            title = "Collaboration Pods",
                            subtitle = "Smart pods for hybrid collaboration",
                            meta = "Auto-release after 15 min idle"
                        )
                    )
                )
            )
        ),
        ResourceSection(
            title = "Status",
            tabs = listOf(
                ResourceSectionTab(
                    label = "Available",
                    count = 14,
                    content = listOf(
                        ResourceContentItem(
                            title = "Innovation Pod 02",
                            subtitle = "Configured for quick huddles",
                            meta = "Open until 8 PM"
                        )
                    )
                ),
                ResourceSectionTab(
                    label = "In Use",
                    count = 9,
                    content = listOf(
                        ResourceContentItem(
                            title = "Composite Lab",
                            subtitle = "Live booking by Mechanical Engineering",
                            meta = "Ends at 16:30"
                        )
                    )
                ),
                ResourceSectionTab(
                    label = "Under Maintenance",
                    count = 3,
                    content = listOf(
                        ResourceContentItem(
                            title = "Spectrometer",
                            subtitle = "Awaiting calibration vendor",
                            meta = "ETA: Thursday"
                        )
                    )
                ),
                ResourceSectionTab(
                    label = "Reserved",
                    count = 6,
                    content = listOf(
                        ResourceContentItem(
                            title = "Board Room",
                            subtitle = "Reserved for placement briefing",
                            meta = "Tomorrow 10 AM"
                        )
                    )
                )
            )
        ),
        ResourceSection(
            title = "Urgency",
            tabs = listOf(
                ResourceSectionTab(
                    label = "High Priority",
                    count = 5,
                    content = listOf(
                        ResourceContentItem(
                            title = "Critical Server Patch",
                            subtitle = "Security fix rollout pending",
                            meta = "Action in next 4 hours"
                        )
                    )
                ),
                ResourceSectionTab(
                    label = "Medium / Low",
                    count = 11,
                    content = listOf(
                        ResourceContentItem(
                            title = "Furniture Refresh",
                            subtitle = "Ergonomic chairs for labs",
                            meta = "Scheduled next week"
                        )
                    )
                ),
                ResourceSectionTab(
                    label = "Critical",
                    count = 2,
                    content = listOf(
                        ResourceContentItem(
                            title = "Power Back-up Audit",
                            subtitle = "Generator load tests pending",
                            meta = "Block access until cleared"
                        )
                    )
                )
            )
        ),
        ResourceSection(
            title = "History & Logs",
            tabs = listOf(
                ResourceSectionTab(
                    label = "Usage Log",
                    count = 24,
                    content = listOf(
                        ResourceContentItem(
                            title = "Lab Space A-101",
                            subtitle = "Booked by CSE Dept",
                            meta = "Logged: 11:30 AM"
                        )
                    )
                ),
                ResourceSectionTab(
                    label = "User Activity",
                    count = 16,
                    content = listOf(
                        ResourceContentItem(
                            title = "Facility Manager",
                            subtitle = "Approved 3 requests",
                            meta = "Last login 09:12 AM"
                        )
                    )
                ),
                ResourceSectionTab(
                    label = "Machine History",
                    count = 12,
                    content = listOf(
                        ResourceContentItem(
                            title = "CNC Router",
                            subtitle = "Runtime stats updated",
                            meta = "Last service 28 days ago"
                        )
                    )
                ),
                ResourceSectionTab(
                    label = "System Logs",
                    count = 8,
                    content = listOf(
                        ResourceContentItem(
                            title = "Integration Sync",
                            subtitle = "Third-party inventory updated",
                            meta = "Status: Success"
                        )
                    )
                )
            )
        ),
        ResourceSection(
            title = "Recently Added",
            tabs = listOf(
                ResourceSectionTab(
                    label = "New Machines",
                    count = recentlyAddedItems.count { it.title.contains("Lab", ignoreCase = true) || it.subtitle.contains("Equipment", ignoreCase = true) },
                    content = recentlyAddedItems.take(2)
                ),
                ResourceSectionTab(
                    label = "New Users",
                    count = 4,
                    content = listOf(
                        ResourceContentItem(
                            title = "Workshop Technicians",
                            subtitle = "4 technicians onboarded",
                            meta = "Clearance complete"
                        )
                    )
                ),
                ResourceSectionTab(
                    label = "New Departments",
                    count = 1,
                    content = listOf(
                        ResourceContentItem(
                            title = "Sustainability Cell",
                            subtitle = "Green initiatives task force",
                            meta = "Workspace allocated"
                        )
                    )
                )
            )
        )
    )
}

data class FilterTag(
    val section: String,
    val label: String,
    val count: Int
)

data class ResourceSection(
    val title: String,
    val tabs: List<ResourceSectionTab>
) {
    val totalCount: Int = tabs.sumOf { it.count }
}

data class ResourceSectionTab(
    val label: String,
    val count: Int,
    val content: List<ResourceContentItem> = emptyList(),
    val subTabs: List<ResourceSectionSubTab> = emptyList()
)

data class ResourceSectionSubTab(
    val label: String,
    val count: Int,
    val content: List<ResourceContentItem> = emptyList(),
    val propertyRequests: List<PropertyRequest> = emptyList(),
    val showPriorityTag: Boolean = false,
    val showQuickToggle: Boolean = false,
    val showVerificationAction: Boolean = false
)

data class ResourceSectionState(
    val selectedTab: ResourceSectionTab? = null,
    val selectedSubTab: ResourceSectionSubTab? = null
)

data class ResourceContentItem(
    val title: String,
    val subtitle: String,
    val meta: String
)

// Resource data class
data class Resource(
    val name: String,
    val type: String,
    val capacity: String,
    val currentUsage: String,
    val status: String,
    val location: String
)

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

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController : NavHostController = rememberNavController()
    HomeScreen(navController = navController)
}
