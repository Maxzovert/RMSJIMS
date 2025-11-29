package com.example.rmsjims.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rmsjims.ui.components.AppButton
import com.example.rmsjims.ui.components.AppTextField
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
fun SystemSettingScreen(
    navController: NavHostController
) {
    // Placeholder settings
    val settings = listOf(
        Setting(
            category = "General",
            items = listOf(
                SettingItem("System Name", "JIMS Resource Management", "String"),
                SettingItem("Auto-approval", "Enabled", "Boolean"),
                SettingItem("Notification", "Enabled", "Boolean"),
                SettingItem("Maintenance Mode", "Disabled", "Boolean")
            )
        ),
        Setting(
            category = "Booking",
            items = listOf(
                SettingItem("Max Booking Days", "30", "Number"),
                SettingItem("Advance Booking Days", "7", "Number"),
                SettingItem("Auto-cancel Days", "3", "Number")
            )
        ),
        Setting(
            category = "Notifications",
            items = listOf(
                SettingItem("Email Notifications", "Enabled", "Boolean"),
                SettingItem("SMS Notifications", "Disabled", "Boolean"),
                SettingItem("Push Notifications", "Enabled", "Boolean")
            )
        ),
        Setting(
            category = "Security",
            items = listOf(
                SettingItem("Session Timeout", "30 minutes", "String"),
                SettingItem("Password Policy", "Strong", "String"),
                SettingItem("Two-Factor Auth", "Disabled", "Boolean")
            )
        )
    )

    // Role permissions state (placeholder toggles for UI only)
    var adminCanApprove by remember { mutableStateOf(true) }
    var staffCanApprove by remember { mutableStateOf(false) }
    var assistantCanApprove by remember { mutableStateOf(false) }

    // Email notifications toggles
    var notifyMaintenance by remember { mutableStateOf(true) }
    var notifyRaisedTickets by remember { mutableStateOf(true) }
    var notifyEquipmentBookings by remember { mutableStateOf(true) }
    var notifyBookingChanges by remember { mutableStateOf(true) }
    var notifyEquipmentStatus by remember { mutableStateOf(false) }

    // Booking rules state (UI only)
    var maxBookingDuration by remember { mutableStateOf("30 days") }
    var approvalFlow by remember { mutableStateOf("Admin Approval") }
    var sessionTimeout by remember { mutableStateOf("30 minutes") }
    var defaultEquipmentStatus by remember { mutableStateOf("Online") }

    // Maintenance & bulk actions state
    var autoMaintenanceNotifications by remember { mutableStateOf(true) }

    // Protected staff credentials state (UI only)
    var showStaffCredentials by remember { mutableStateOf(false) }
    var showStaffPinPrompt by remember { mutableStateOf(false) }
    var staffPin by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "System Settings",
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
                items(settings) { setting ->
                    SettingCategoryCard(setting = setting)
                }

                // Role Permissions Section
                item {
                    RolePermissionsSection(
                        adminCanApprove = adminCanApprove,
                        onAdminCanApproveChange = { adminCanApprove = it },
                        staffCanApprove = staffCanApprove,
                        onStaffCanApproveChange = { staffCanApprove = it },
                        assistantCanApprove = assistantCanApprove,
                        onAssistantCanApproveChange = { assistantCanApprove = it }
                    )
                }

                // Password-protected staff credentials section
                item {
                    StaffCredentialsProtectedSection(
                        showCredentials = showStaffCredentials,
                        onUnlockClick = { showStaffPinPrompt = true }
                    )
                }

                // Email Notification Toggles
                item {
                    EmailNotificationSettingsSection(
                        notifyMaintenance = notifyMaintenance,
                        onNotifyMaintenanceChange = { notifyMaintenance = it },
                        notifyRaisedTickets = notifyRaisedTickets,
                        onNotifyRaisedTicketsChange = { notifyRaisedTickets = it },
                        notifyEquipmentBookings = notifyEquipmentBookings,
                        onNotifyEquipmentBookingsChange = { notifyEquipmentBookings = it },
                        notifyBookingChanges = notifyBookingChanges,
                        onNotifyBookingChangesChange = { notifyBookingChanges = it },
                        notifyEquipmentStatus = notifyEquipmentStatus,
                        onNotifyEquipmentStatusChange = { notifyEquipmentStatus = it }
                    )
                }

                // Booking Rules
                item {
                    BookingRulesSection(
                        maxBookingDuration = maxBookingDuration,
                        onMaxBookingDurationChange = { maxBookingDuration = it },
                        approvalFlow = approvalFlow,
                        onApprovalFlowChange = { approvalFlow = it },
                        sessionTimeout = sessionTimeout,
                        onSessionTimeoutChange = { sessionTimeout = it },
                        defaultEquipmentStatus = defaultEquipmentStatus,
                        onDefaultEquipmentStatusChange = { defaultEquipmentStatus = it }
                    )
                }

                // Bulk Actions
                item {
                    BulkActionsSection()
                }

                // Maintenance Options
                item {
                    MaintenanceOptionsSection(
                        autoMaintenanceNotifications = autoMaintenanceNotifications,
                        onAutoMaintenanceNotificationsChange = { autoMaintenanceNotifications = it }
                    )
                }
                
                item {
                    Spacer(modifier = Modifier.height(ResponsiveLayout.getVerticalPadding()))
                    
                    // Save Button
                    AppButton(
                        buttonText = "Save Changes",
                        onClick = { },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    if (showStaffPinPrompt) {
        StaffPinDialog(
            pin = staffPin,
            onPinChange = { staffPin = it },
            onConfirm = {
                // In a real app, validate PIN here
                if (staffPin.isNotEmpty()) {
                    showStaffCredentials = true
                    showStaffPinPrompt = false
                    staffPin = ""
                }
            },
            onDismiss = {
                showStaffPinPrompt = false
                staffPin = ""
            }
        )
    }
}

@Composable
fun SettingCategoryCard(setting: Setting) {
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
            )
        ) {
            CustomLabel(
                header = setting.category,
                fontSize = ResponsiveLayout.getResponsiveFontSize(18.sp, 20.sp, 22.sp),
                headerColor = primaryColor
            )
            
            Spacer(modifier = Modifier.height(pxToDp(12)))
            
            Column(
                verticalArrangement = Arrangement.spacedBy(pxToDp(12))
            ) {
                setting.items.forEachIndexed { index, item ->
                    SettingItemRow(settingItem = item)
                    if (index < setting.items.size - 1) {
                        Divider(
                            thickness = pxToDp(1),
                            color = cardColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingItemRow(settingItem: SettingItem) {
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
                header = settingItem.name,
                fontSize = ResponsiveLayout.getResponsiveFontSize(14.sp, 16.sp, 18.sp),
                headerColor = onSurfaceColor
            )
            if (settingItem.description != null) {
                CustomLabel(
                    header = settingItem.description,
                    fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                    headerColor = onSurfaceColor.copy(0.6f)
                )
            }
        }
        
        when (settingItem.type) {
            "Boolean" -> {
                AppButton(
                    buttonText = settingItem.value,
                    onClick = { },
                    modifier = Modifier.padding(start = pxToDp(16))
                )
            }
            else -> {
                CustomLabel(
                    header = settingItem.value,
                    fontSize = ResponsiveLayout.getResponsiveFontSize(14.sp, 16.sp, 18.sp),
                    headerColor = primaryColor,
                    modifier = Modifier.padding(start = pxToDp(16))
                )
                AppButton(
                    buttonText = "Edit",
                    onClick = { },
                    modifier = Modifier.padding(start = pxToDp(8))
                )
            }
        }
    }
}

// Placeholder data classes
data class Setting(
    val category: String,
    val items: List<SettingItem>
)

data class SettingItem(
    val name: String,
    val value: String,
    val type: String,
    val description: String? = null
)

// --- New sections ---

@Composable
fun RolePermissionsSection(
    adminCanApprove: Boolean,
    onAdminCanApproveChange: (Boolean) -> Unit,
    staffCanApprove: Boolean,
    onStaffCanApproveChange: (Boolean) -> Unit,
    assistantCanApprove: Boolean,
    onAssistantCanApproveChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = onSurfaceVariant),
        shape = RectangleShape
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
                header = "Role Permissions",
                fontSize = ResponsiveLayout.getResponsiveFontSize(18.sp, 20.sp, 22.sp),
                headerColor = onSurfaceColor
            )
            CustomLabel(
                header = "Control who can approve bookings and manage equipment.",
                fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                headerColor = onSurfaceColor.copy(0.7f)
            )

            RolePermissionRow(
                role = "Admin",
                description = "Full access to approve and override bookings.",
                checked = adminCanApprove,
                onCheckedChange = onAdminCanApproveChange
            )
            RolePermissionRow(
                role = "Staff",
                description = "Can approve bookings for their department.",
                checked = staffCanApprove,
                onCheckedChange = onStaffCanApproveChange
            )
            RolePermissionRow(
                role = "Assistant",
                description = "Can suggest approvals but requires Admin/Staff confirmation.",
                checked = assistantCanApprove,
                onCheckedChange = onAssistantCanApproveChange
            )
        }
    }
}

@Composable
private fun RolePermissionRow(
    role: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
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
                header = role,
                fontSize = ResponsiveLayout.getResponsiveFontSize(14.sp, 16.sp, 18.sp),
                headerColor = onSurfaceColor
            )
            CustomLabel(
                header = description,
                fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                headerColor = onSurfaceColor.copy(0.7f)
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun StaffCredentialsProtectedSection(
    showCredentials: Boolean,
    onUnlockClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = onSurfaceVariant),
        shape = RectangleShape
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
                header = "Staff Credentials (Protected)",
                fontSize = ResponsiveLayout.getResponsiveFontSize(18.sp, 20.sp, 22.sp),
                headerColor = onSurfaceColor
            )
            CustomLabel(
                header = "View staff user IDs and passwords. Admin PIN required.",
                fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                headerColor = onSurfaceColor.copy(0.7f)
            )

            if (!showCredentials) {
                AppButton(
                    buttonText = "Unlock",
                    onClick = onUnlockClick,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Spacer(modifier = Modifier.height(pxToDp(8)))
                val credentials = listOf(
                    "staff01 • staff01@edu.com • ••••••••",
                    "lab_asst02 • lab2@edu.com • ••••••••",
                    "staff_admin • admin@edu.com • ••••••••"
                )
                credentials.forEach { line ->
                    CustomLabel(
                        header = "• $line",
                        fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                        headerColor = onSurfaceColor.copy(0.8f),
                        modifier = Modifier.padding(start = pxToDp(8))
                    )
                }
            }
        }
    }
}

@Composable
fun StaffPinDialog(
    pin: String,
    onPinChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            CustomLabel(
                header = "Enter Admin PIN",
                fontSize = ResponsiveLayout.getResponsiveFontSize(18.sp, 20.sp, 22.sp),
                headerColor = onSurfaceColor
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(pxToDp(12))
            ) {
                CustomLabel(
                    header = "This section is protected. Enter your admin dashboard PIN to continue.",
                    fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                    headerColor = onSurfaceColor.copy(0.8f)
                )
                AppTextField(
                    value = pin,
                    onValueChange = onPinChange,
                    placeholder = "Admin PIN"
                )
            }
        },
        confirmButton = {
            AppButton(
                buttonText = "Confirm",
                onClick = onConfirm
            )
        },
        dismissButton = {
            AppButton(
                buttonText = "Cancel",
                onClick = onDismiss,
                containerColor = cardColor,
                contentColor = onSurfaceColor
            )
        },
        containerColor = whiteColor
    )
}

@Composable
fun EmailNotificationSettingsSection(
    notifyMaintenance: Boolean,
    onNotifyMaintenanceChange: (Boolean) -> Unit,
    notifyRaisedTickets: Boolean,
    onNotifyRaisedTicketsChange: (Boolean) -> Unit,
    notifyEquipmentBookings: Boolean,
    onNotifyEquipmentBookingsChange: (Boolean) -> Unit,
    notifyBookingChanges: Boolean,
    onNotifyBookingChangesChange: (Boolean) -> Unit,
    notifyEquipmentStatus: Boolean,
    onNotifyEquipmentStatusChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = onSurfaceVariant),
        shape = RectangleShape
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
                header = "Email Notifications",
                fontSize = ResponsiveLayout.getResponsiveFontSize(18.sp, 20.sp, 22.sp),
                headerColor = onSurfaceColor
            )
            CustomLabel(
                header = "Configure which events send email updates to admins and staff.",
                fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                headerColor = onSurfaceColor.copy(0.7f)
            )

            NotificationToggleRow(
                label = "Maintenance updates",
                checked = notifyMaintenance,
                onCheckedChange = onNotifyMaintenanceChange
            )
            NotificationToggleRow(
                label = "Raised tickets",
                checked = notifyRaisedTickets,
                onCheckedChange = onNotifyRaisedTicketsChange
            )
            NotificationToggleRow(
                label = "Equipment bookings",
                checked = notifyEquipmentBookings,
                onCheckedChange = onNotifyEquipmentBookingsChange
            )
            NotificationToggleRow(
                label = "Booking changes / cancellations",
                checked = notifyBookingChanges,
                onCheckedChange = onNotifyBookingChangesChange
            )
            NotificationToggleRow(
                label = "Equipment status changes",
                checked = notifyEquipmentStatus,
                onCheckedChange = onNotifyEquipmentStatusChange
            )
        }
    }
}

@Composable
private fun NotificationToggleRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomLabel(
            header = label,
            fontSize = ResponsiveLayout.getResponsiveFontSize(14.sp, 16.sp, 18.sp),
            headerColor = onSurfaceColor
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun BookingRulesSection(
    maxBookingDuration: String,
    onMaxBookingDurationChange: (String) -> Unit,
    approvalFlow: String,
    onApprovalFlowChange: (String) -> Unit,
    sessionTimeout: String,
    onSessionTimeoutChange: (String) -> Unit,
    defaultEquipmentStatus: String,
    onDefaultEquipmentStatusChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = onSurfaceVariant),
        shape = RectangleShape
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
                header = "Booking Rules",
                fontSize = ResponsiveLayout.getResponsiveFontSize(18.sp, 20.sp, 22.sp),
                headerColor = onSurfaceColor
            )

            BookingRuleRow(
                label = "Max booking duration",
                value = maxBookingDuration,
                onValueChange = onMaxBookingDurationChange
            )
            BookingRuleRow(
                label = "Approval flow",
                value = approvalFlow,
                onValueChange = onApprovalFlowChange
            )
            BookingRuleRow(
                label = "Session timeout",
                value = sessionTimeout,
                onValueChange = onSessionTimeoutChange
            )
            BookingRuleRow(
                label = "Default equipment status",
                value = defaultEquipmentStatus,
                onValueChange = onDefaultEquipmentStatusChange
            )
        }
    }
}

@Composable
private fun BookingRuleRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(pxToDp(4))
    ) {
        CustomLabel(
            header = label,
            fontSize = ResponsiveLayout.getResponsiveFontSize(14.sp, 16.sp, 18.sp),
            headerColor = onSurfaceColor
        )
        AppTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = label
        )
    }
}

@Composable
fun BulkActionsSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = onSurfaceVariant),
        shape = RectangleShape
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
                header = "Bulk Actions",
                fontSize = ResponsiveLayout.getResponsiveFontSize(18.sp, 20.sp, 22.sp),
                headerColor = onSurfaceColor
            )
            CustomLabel(
                header = "Run high-impact operations across multiple equipment items.",
                fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                headerColor = onSurfaceColor.copy(0.7f)
            )

            AppButton(
                buttonText = "Bulk Delete Equipment",
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                containerColor = cardColor,
                contentColor = onSurfaceColor
            )
            AppButton(
                buttonText = "Bulk Assign Tags (Low Priority / Under Maintenance / Offline)",
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun MaintenanceOptionsSection(
    autoMaintenanceNotifications: Boolean,
    onAutoMaintenanceNotificationsChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = onSurfaceVariant),
        shape = RectangleShape
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
                header = "Maintenance Options",
                fontSize = ResponsiveLayout.getResponsiveFontSize(18.sp, 20.sp, 22.sp),
                headerColor = onSurfaceColor
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(pxToDp(4))
                ) {
                    CustomLabel(
                        header = "Automatic maintenance notifications",
                        fontSize = ResponsiveLayout.getResponsiveFontSize(14.sp, 16.sp, 18.sp),
                        headerColor = onSurfaceColor
                    )
                    CustomLabel(
                        header = "Alert admins when equipment is overdue for maintenance.",
                        fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                        headerColor = onSurfaceColor.copy(0.7f)
                    )
                }
                Switch(
                    checked = autoMaintenanceNotifications,
                    onCheckedChange = onAutoMaintenanceNotificationsChange
                )
            }

            Divider(thickness = pxToDp(1), color = cardColor)

            CustomLabel(
                header = "Manual backup",
                fontSize = ResponsiveLayout.getResponsiveFontSize(14.sp, 16.sp, 18.sp),
                headerColor = onSurfaceColor
            )
            CustomLabel(
                header = "Trigger a one-time backup of equipment data.",
                fontSize = ResponsiveLayout.getResponsiveFontSize(12.sp, 14.sp, 16.sp),
                headerColor = onSurfaceColor.copy(0.7f)
            )
            AppButton(
                buttonText = "Run Backup Now",
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SystemSettingScreenPreview() {
    val navController = rememberNavController()
    SystemSettingScreen(navController = navController)
}
