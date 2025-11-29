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
fun TicketManagementScreen(
    navController: NavHostController
) {
    // Placeholder tickets
    val tickets = listOf(
        Ticket(
            id = "TICK-2024-001",
            title = "Equipment Not Working",
            description = "Oscilloscope in Lab B-205 is not displaying correctly. Need immediate attention.",
            raisedBy = "Dr. Ravi Kumar",
            priority = "High",
            status = "Open",
            createdDate = "2024-01-20",
            assignedTo = "Akash Singh"
        ),
        Ticket(
            id = "TICK-2024-002",
            title = "Maintenance Request",
            description = "3D Printer needs regular maintenance and calibration.",
            raisedBy = "Prof. Meera Sharma",
            priority = "Medium",
            status = "In Progress",
            createdDate = "2024-01-19",
            assignedTo = "Akash Singh"
        ),
        Ticket(
            id = "TICK-2024-003",
            title = "Equipment Setup Help",
            description = "Need assistance setting up new DNA Sequencer in Lab C-301.",
            raisedBy = "Dr. Amit Patel",
            priority = "Low",
            status = "Open",
            createdDate = "2024-01-18",
            assignedTo = "Unassigned"
        ),
        Ticket(
            id = "TICK-2024-004",
            title = "Equipment Malfunction",
            description = "Server in Lab A-101 is overheating. Cooling system issue.",
            raisedBy = "Sunita Reddy",
            priority = "High",
            status = "Resolved",
            createdDate = "2024-01-17",
            assignedTo = "Akash Singh"
        )
    )

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Ticket Management",
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
            
            // Summary Cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ResponsiveLayout.getHorizontalPadding()),
                horizontalArrangement = Arrangement.spacedBy(ResponsiveLayout.getCardSpacing())
            ) {
                SummaryCard("Open", "${tickets.count { it.status == "Open" }}", Color(0xFFE64646))
                SummaryCard("In Progress", "${tickets.count { it.status == "In Progress" }}", Color(0xFFE67824))
                SummaryCard("Resolved", "${tickets.count { it.status == "Resolved" }}", Color(0xFF26BB64C))
            }
            
            Spacer(modifier = Modifier.height(ResponsiveLayout.getVerticalPadding()))
            
            // Tickets List
            CustomLabel(
                header = "All Tickets",
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
                items(tickets) { ticket ->
                    TicketCard(ticket = ticket)
                }
            }
        }
    }
}

@Composable
fun SummaryCard(label: String, value: String, color: Color) {
    Card(
        modifier = Modifier,
        colors = CardDefaults.cardColors(
            containerColor = onSurfaceVariant
        ),
        shape = RectangleShape
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = ResponsiveLayout.getResponsivePadding(12.dp, 16.dp, 20.dp),
                vertical = ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp)
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(pxToDp(8))
        ) {
            CustomLabel(
                header = value,
                fontSize = ResponsiveLayout.getResponsiveFontSize(20.sp, 24.sp, 28.sp),
                headerColor = color
            )
            CustomLabel(
                header = label,
                fontSize = ResponsiveLayout.getResponsiveFontSize(10.sp, 12.sp, 14.sp),
                headerColor = onSurfaceColor.copy(0.7f)
            )
        }
    }
}

@Composable
fun TicketCard(ticket: Ticket) {
    val statusColor = when (ticket.status) {
        "Open" -> Color(0xFFE64646)
        "In Progress" -> Color(0xFFE67824)
        "Resolved" -> Color(0xFF26BB64C)
        else -> onSurfaceColor
    }
    
    val priorityColor = when (ticket.priority) {
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
                        header = ticket.title,
                        fontSize = ResponsiveLayout.getResponsiveFontSize(16.sp, 18.sp, 20.sp),
                        headerColor = onSurfaceColor
                    )
                    CustomLabel(
                        header = "ID: ${ticket.id}",
                        fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                        headerColor = onSurfaceColor.copy(0.6f)
                    )
                }
                
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(pxToDp(8))
                ) {
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
                            header = ticket.priority,
                            fontSize = ResponsiveLayout.getResponsiveFontSize(10.sp, 12.sp, 14.sp),
                            headerColor = priorityColor
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
                            header = ticket.status,
                            fontSize = ResponsiveLayout.getResponsiveFontSize(10.sp, 12.sp, 14.sp),
                            headerColor = statusColor
                        )
                    }
                }
            }
            
            // Description
            CustomLabel(
                header = ticket.description,
                fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                headerColor = onSurfaceColor.copy(0.7f)
            )
            
            // Details
            Column(
                verticalArrangement = Arrangement.spacedBy(pxToDp(8))
            ) {
                DetailRow("Raised By", ticket.raisedBy)
                DetailRow("Assigned To", ticket.assignedTo)
                DetailRow("Created Date", ticket.createdDate)
            }
            
            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(pxToDp(12))
            ) {
                AppButton(
                    buttonText = "View Details",
                    onClick = { },
                    modifier = Modifier.weight(1f)
                )
                AppButton(
                    buttonText = "Assign",
                    onClick = { },
                    modifier = Modifier.weight(1f)
                )
                if (ticket.status != "Resolved") {
                    AppButton(
                        buttonText = "Resolve",
                        onClick = { },
                        modifier = Modifier.weight(1f),
                        containerColor = Color(0xFF26BB64C),
                        contentColor = whiteColor
                    )
                }
            }
        }
    }
}


// Placeholder data class
data class Ticket(
    val id: String,
    val title: String,
    val description: String,
    val raisedBy: String,
    val priority: String,
    val status: String,
    val createdDate: String,
    val assignedTo: String
)

@Preview(showBackground = true)
@Composable
fun TicketManagementScreenPreview() {
    val navController = rememberNavController()
    TicketManagementScreen(navController = navController)
}
