package com.example.rmsjims.ui.screens.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rmsjims.R
import com.example.rmsjims.navigation.Screen
import com.example.rmsjims.ui.components.AppCircularIcon
import com.example.rmsjims.ui.components.AppNavIcon
import com.example.rmsjims.ui.components.AppSearchBar
import com.example.rmsjims.ui.components.CustomLabel
import com.example.rmsjims.ui.components.CustomNavigationBar
import com.example.rmsjims.ui.components.AppLogoImage
import com.example.rmsjims.ui.theme.onSurfaceColor
import com.example.rmsjims.ui.theme.onSurfaceVariant
import com.example.rmsjims.ui.theme.primaryColor
import com.example.rmsjims.ui.theme.whiteColor
import com.example.rmsjims.ui.theme.errorColor
import com.example.rmsjims.ui.theme.headerColor
import com.example.rmsjims.util.ResponsiveLayout
import com.example.rmsjims.util.pxToDp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminDashboardScreen(
    navController: NavHostController,
    adminName: String = "Admin" // Default admin name, can be passed from ViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    var fabExpanded by remember { mutableStateOf(false) }
    
    // Organization info
    val organizationName = "GYMS"
    val location = "Mumbai, India" // Placeholder
    val currentDate = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault()).format(Date())
    
    // Functionality cards
    val functionalityCards = listOf(
        FunctionalityCard(
            title = "Equipment Management",
            iconRes = R.drawable.ic_storage,
            route = Screen.EquipmentManagementScreen.route
        ),
        FunctionalityCard(
            title = "Booking Management",
            iconRes = R.drawable.ic_ticket_thread,
            route = Screen.AdminBookingsScreen.route
        ),
        FunctionalityCard(
            title = "User Management",
            iconRes = R.drawable.ic_edit,
            route = Screen.UserManagementScreen.route
        ),
        FunctionalityCard(
            title = "System Settings",
            iconRes = R.drawable.ic_vector,
            route = Screen.SystemSettingScreen.route
        ),
        FunctionalityCard(
            title = "Operations & Costs",
            iconRes = R.drawable.ic_assigned_time,
            route = Screen.ResourceManagementScreen.route
        ),
        FunctionalityCard(
            title = "Logs, Audits & Tickets",
            iconRes = R.drawable.ic_chat,
            route = Screen.TicketManagementScreen.route
        )
    )

    Scaffold(
        topBar = {
            AdminDashboardTopBar(
                onNotificationClick = { /* Handle notification click */ },
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it }
            )
        },
        bottomBar = {
            CustomNavigationBar(navController = navController)
        },
        floatingActionButton = {
            AdminFAB(
                expanded = fabExpanded,
                onExpandedChange = { fabExpanded = it },
                onAddEquipment = { 
                    fabExpanded = false
                    navController.navigate(Screen.NewEquipmentScreen.route)
                },
                onAddUser = { 
                    fabExpanded = false
                    navController.navigate(Screen.UserManagementScreen.route)
                },
                onAddBooking = { 
                    fabExpanded = false
                    navController.navigate(Screen.AdminBookingsScreen.route)
                }
            )
        },
        containerColor = whiteColor
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                horizontal = ResponsiveLayout.getHorizontalPadding(),
                vertical = ResponsiveLayout.getVerticalPadding()
            ),
            verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getResponsivePadding(20.dp, 24.dp, 28.dp))
        ) {
            // Greeting Section
            item {
                GreetingSection(adminName = adminName)
            }
            
            // Intro Card / Organization Info
            item {
                OrganizationInfoOutlineBox(
                    organizationName = organizationName,
                    location = location,
                    currentDate = currentDate
                )
            }
            
            // Main Functionality Cards Section Header
            item {
                CustomLabel(
                    header = "Quick Actions",
                    fontSize = ResponsiveLayout.getResponsiveFontSize(18.sp, 20.sp, 22.sp),
                    headerColor = onSurfaceColor,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            // Main Functionality Cards Grid (2 columns × 3 rows)
            item {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(0.dp),
                    verticalArrangement = ResponsiveLayout.getVerticalGridArrangement(),
                    horizontalArrangement = ResponsiveLayout.getGridArrangement(),
                    modifier = Modifier.height(ResponsiveLayout.getResponsiveSize(480.dp, 540.dp, 600.dp))
                ) {
                    items(functionalityCards) { card ->
                        FunctionalityCardItem(
                            card = card,
                            onClick = { navController.navigate(card.route) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AdminDashboardTopBar(
    onNotificationClick: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(whiteColor)
            .padding(
                horizontal = ResponsiveLayout.getHorizontalPadding(),
                vertical = ResponsiveLayout.getResponsivePadding(15.dp, 18.dp, 22.dp)
            )
    ) {
        // Title and Notification Icon Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Spacer to center the title
            Spacer(modifier = Modifier.width(ResponsiveLayout.getResponsiveSize(24.dp, 28.dp, 32.dp)))
            
            // Centered Title
            CustomLabel(
                header = "Admin Dashboard",
                fontSize = ResponsiveLayout.getResponsiveFontSize(25.sp, 28.sp, 32.sp),
                headerColor = headerColor,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            
            // Notification Icon
            AppCircularIcon(
                painter = painterResource(R.drawable.ic_mail), // Using available icon, replace with notification icon if available
                iconDescription = "Notifications",
                onClick = onNotificationClick,
                tint = onSurfaceColor
            )
        }
        
        Spacer(modifier = Modifier.height(ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp)))
        
        // Search Bar
        AppSearchBar(
            query = searchQuery,
            onQueryChange = onSearchQueryChange,
            placeholder = "Search equipment..."
        )
    }
}

@Composable
fun GreetingSection(adminName: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(pxToDp(4))
    ) {
        CustomLabel(
            header = "Hi, $adminName",
            fontSize = ResponsiveLayout.getResponsiveFontSize(24.sp, 28.sp, 32.sp),
            headerColor = onSurfaceColor,
            fontWeight = FontWeight.SemiBold
        )
        CustomLabel(
            header = "Check your dashboard tasks",
            fontSize = ResponsiveLayout.getResponsiveFontSize(14.sp, 16.sp, 18.sp),
            headerColor = onSurfaceColor.copy(0.7f)
        )
    }
}

@Composable
fun OrganizationInfoOutlineBox(
    organizationName: String,
    location: String,
    currentDate: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = ResponsiveLayout.getResponsivePadding(1.dp, 1.5.dp, 2.dp),
                color = onSurfaceColor.copy(0.2f),
                shape = RoundedCornerShape(ResponsiveLayout.getResponsiveSize(8.dp, 12.dp, 16.dp))
            )
            .padding(ResponsiveLayout.getResponsivePadding(20.dp, 24.dp, 28.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getResponsivePadding(8.dp, 10.dp, 12.dp))
            ) {
                CustomLabel(
                    header = organizationName,
                    fontSize = ResponsiveLayout.getResponsiveFontSize(20.sp, 24.sp, 28.sp),
                    headerColor = onSurfaceColor,
                    fontWeight = FontWeight.SemiBold
                )
                CustomLabel(
                    header = location,
                    fontSize = ResponsiveLayout.getResponsiveFontSize(14.sp, 16.sp, 18.sp),
                    headerColor = onSurfaceColor.copy(0.7f)
                )
                CustomLabel(
                    header = currentDate,
                    fontSize = ResponsiveLayout.getResponsiveFontSize(14.sp, 16.sp, 18.sp),
                    headerColor = onSurfaceColor.copy(0.8f)
                )
            }
            
            // App Logo
            Image(
                painter = painterResource(R.drawable.jims_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(ResponsiveLayout.getResponsiveSize(80.dp, 100.dp, 120.dp))
                    .clip(RoundedCornerShape(ResponsiveLayout.getResponsiveSize(8.dp, 12.dp, 16.dp))),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun FunctionalityCardItem(
    card: FunctionalityCard,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(ResponsiveLayout.getResponsiveSize(140.dp, 160.dp, 180.dp))
            .shadow(
                elevation = ResponsiveLayout.getResponsiveSize(2.dp, 3.dp, 4.dp),
                shape = RoundedCornerShape(ResponsiveLayout.getResponsiveSize(12.dp, 16.dp, 20.dp))
            ),
        colors = CardDefaults.cardColors(containerColor = onSurfaceVariant),
        shape = RoundedCornerShape(ResponsiveLayout.getResponsiveSize(12.dp, 16.dp, 20.dp)),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = pxToDp(3))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            onSurfaceVariant,
                            onSurfaceVariant.copy(alpha = 0.95f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    color = primaryColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(ResponsiveLayout.getResponsiveSize(14.dp, 16.dp, 18.dp))
                ) {
                    Box(
                        modifier = Modifier.padding(ResponsiveLayout.getResponsivePadding(12.dp, 14.dp, 16.dp))
                    ) {
                        AppNavIcon(
                            painter = painterResource(id = card.iconRes),
                            iconDescription = card.title,
                            tint = primaryColor,
                            iconSize = ResponsiveLayout.getResponsiveSize(32.dp, 36.dp, 40.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(ResponsiveLayout.getResponsivePadding(14.dp, 16.dp, 18.dp)))
                
                CustomLabel(
                    header = card.title,
                    fontSize = ResponsiveLayout.getResponsiveFontSize(14.sp, 16.sp, 18.sp),
                    headerColor = onSurfaceColor,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth(),
                    maxLine = 2
                )
            }
        }
    }
}

@Composable
fun AdminFAB(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onAddEquipment: () -> Unit,
    onAddUser: () -> Unit,
    onAddBooking: () -> Unit
) {
    Box {
        FloatingActionButton(
            onClick = { onExpandedChange(!expanded) },
            containerColor = primaryColor,
            contentColor = whiteColor,
            shape = CircleShape,
            modifier = Modifier.size(ResponsiveLayout.getResponsiveSize(56.dp, 64.dp, 72.dp))
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                modifier = Modifier.size(ResponsiveLayout.getResponsiveSize(24.dp, 28.dp, 32.dp))
            )
        }
        
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier.background(whiteColor)
        ) {
            DropdownMenuItem(
                text = { 
                    CustomLabel(
                        header = "Add New Equipment",
                        fontSize = ResponsiveLayout.getResponsiveFontSize(14.sp, 16.sp, 18.sp),
                        headerColor = onSurfaceColor
                    )
                },
                onClick = onAddEquipment
            )
            DropdownMenuItem(
                text = { 
                    CustomLabel(
                        header = "Add New User",
                        fontSize = ResponsiveLayout.getResponsiveFontSize(14.sp, 16.sp, 18.sp),
                        headerColor = onSurfaceColor
                    )
                },
                onClick = onAddUser
            )
            DropdownMenuItem(
                text = { 
                    CustomLabel(
                        header = "Add New Booking",
                        fontSize = ResponsiveLayout.getResponsiveFontSize(14.sp, 16.sp, 18.sp),
                        headerColor = onSurfaceColor
                    )
                },
                onClick = onAddBooking
            )
        }
    }
}

// Data classes
data class FunctionalityCard(
    val title: String,
    val iconRes: Int,
    val route: String
)

@Preview(showBackground = true)
@Composable
fun AdminDashboardScreenPreview() {
    val navController = rememberNavController()
    AdminDashboardScreen(navController = navController)
}
