package com.example.rmsjims.ui.screens.staff

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import com.example.rmsjims.R
import com.example.rmsjims.ui.components.AppCategoryIcon
import com.example.rmsjims.ui.components.AppCircularIcon
import com.example.rmsjims.ui.components.CustomLabel
import com.example.rmsjims.ui.components.CustomNavigationBar
import com.example.rmsjims.ui.components.CustomTopBar
import com.example.rmsjims.ui.theme.cardColor
import com.example.rmsjims.ui.theme.categoryIconColor
import com.example.rmsjims.ui.theme.circularBoxColor
import com.example.rmsjims.ui.theme.onSurfaceColor
import com.example.rmsjims.ui.theme.onSurfaceVariant
import com.example.rmsjims.ui.theme.primaryColor
import com.example.rmsjims.ui.theme.whiteColor
import com.example.rmsjims.util.ResponsiveLayout
import com.example.rmsjims.util.pxToDp

@Composable
fun ProfileScreen(
    navController: NavHostController,
){
    androidx.compose.material.Scaffold(
        topBar = {
            CustomTopBar(title = "Profile")
        },
        bottomBar = {
            CustomNavigationBar(navController = navController)
        },
    ) { paddingValues ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(ResponsiveLayout.getHorizontalPadding())
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(ResponsiveLayout.getResponsivePadding(27.dp, 32.dp, 38.dp)))

            // Profile Section (Row Layout)
            UserProfileSection(
                onEditProfilePicture = { /* Handle edit profile picture */ },
                onAddIdentityCard = { /* Handle add identity card */ }
            )

            Spacer(modifier = Modifier.height(ResponsiveLayout.getResponsivePadding(20.dp, 24.dp, 28.dp)))

            // Booking History Section
//            BookingHistorySection()
 
            Spacer(modifier = Modifier.height(ResponsiveLayout.getResponsivePadding(20.dp, 24.dp, 28.dp)))
        }
    }
}

// Profile Section with large rectangular profile picture and person details
@Composable
fun UserProfileSection(
    onEditProfilePicture: () -> Unit = {},
    onAddIdentityCard: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = onSurfaceVariant
        ),
        shape = RectangleShape
    ) {
        Column(
            modifier = Modifier.padding(pxToDp(20)),
            verticalArrangement = Arrangement.spacedBy(pxToDp(20))
        ) {
            // Profile Picture and Person Details Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(pxToDp(20)),
                verticalAlignment = Alignment.Top
            ) {
                // Large rectangular profile picture (editable)
                Box(
                    modifier = Modifier
                        .size(
                            width = ResponsiveLayout.getResponsiveSize(100.dp, 120.dp, 140.dp),
                            height = ResponsiveLayout.getResponsiveSize(120.dp, 140.dp, 160.dp)
                        )
                        .background(
                            color = cardColor.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(pxToDp(12))
                        )
                        .clickable { onEditProfilePicture() }
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_pfp),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(pxToDp(8)),
                        contentScale = ContentScale.Crop
                    )
                    // Edit icon overlay
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(pxToDp(6))
                    ) {
                        AppCircularIcon(
                            painter = painterResource(R.drawable.ic_edit),
                            iconDescription = "Edit Profile Picture",
                            boxSize = pxToDp(28),
                            iconSize = pxToDp(14),
                            iconPadding = pxToDp(4),
                            boxColor = primaryColor,
                            tint = whiteColor,
                            onClick = onEditProfilePicture
                        )
                    }
                }

                // Person Details Column
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(pxToDp(10))
                ) {
                    // Name
                    CustomLabel(
                        header = "Dr. Sumant Rao",
                        fontSize = 20.sp,
                        headerColor = onSurfaceColor,
                        modifier = Modifier.padding(bottom = pxToDp(4))
                    )

                    // Designation
                    CustomLabel(
                        header = "Professor",
                        fontSize = 14.sp,
                        headerColor = onSurfaceColor.copy(0.8f),
                        modifier = Modifier.padding(bottom = pxToDp(2))
                    )

                    // Department
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(pxToDp(8)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppCategoryIcon(
                            painter = painterResource(R.drawable.ic_user),
                            iconDescription = "Department",
                            iconSize = pxToDp(16),
                            tint = categoryIconColor
                        )
                        CustomLabel(
                            header = "Electronics & Communication Engineering",
                            fontSize = 13.sp,
                            headerColor = cardColor
                        )
                    }

                    Spacer(modifier = Modifier.height(pxToDp(12)))

                    // Location Section
                    Column(
                        verticalArrangement = Arrangement.spacedBy(pxToDp(8))
                    ) {
                        // Building
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(pxToDp(8)),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AppCategoryIcon(
                                painter = painterResource(R.drawable.ic_location),
                                iconDescription = "Building",
                                iconSize = pxToDp(16),
                                tint = categoryIconColor
                            )
                            CustomLabel(
                                header = "JIMS Building A, Floor 2",
                                fontSize = 13.sp,
                                headerColor = onSurfaceColor.copy(0.7f)
                            )
                        }

                        // Available Timings
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(pxToDp(8)),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AppCategoryIcon(
                                painter = painterResource(R.drawable.ic_assigned_time),
                                iconDescription = "Timings",
                                iconSize = pxToDp(16),
                                tint = categoryIconColor
                            )
                            CustomLabel(
                                header = "9:00 AM - 6:00 PM",
                                fontSize = 13.sp,
                                headerColor = onSurfaceColor.copy(0.7f)
                            )
                        }

                        // College ID
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(pxToDp(8)),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AppCategoryIcon(
                                painter = painterResource(R.drawable.ic_user),
                                iconDescription = "College ID",
                                iconSize = pxToDp(16),
                                tint = categoryIconColor
                            )
                            CustomLabel(
                                header = "JIMS-EC-2024-001",
                                fontSize = 13.sp,
                                headerColor = onSurfaceColor.copy(0.7f)
                            )
                        }
                    }
                }
            }

            Divider(thickness = pxToDp(1), color = cardColor)

            // Contact Section
            Column(
                verticalArrangement = Arrangement.spacedBy(pxToDp(14))
            ) {
                CustomLabel(
                    header = "Contact",
                    headerColor = onSurfaceColor.copy(0.9f),
                    fontSize = 16.sp
                )

                // Email
                val context = LocalContext.current
                val emailAddress = "sumant.rao@jims.edu.in"
                Row(
                    horizontalArrangement = Arrangement.spacedBy(pxToDp(12)),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AppCircularIcon(
                        painter = painterResource(R.drawable.ic_mail),
                        iconDescription = "Email",
                        boxSize = pxToDp(36),
                        iconSize = pxToDp(18),
                        iconPadding = pxToDp(6),
                        boxColor = circularBoxColor,
                        tint = primaryColor,
                        onClick = {
                            // Copy email to clipboard
                            val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Email", emailAddress)
                            clipboard.setPrimaryClip(clip)
                            
                            // Open mail app with email in 'To' field
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:$emailAddress")
                            }
                            context.startActivity(intent)
                        }
                    )
                    CustomLabel(
                        header = emailAddress,
                        fontSize = 14.sp,
                        headerColor = onSurfaceColor.copy(0.8f),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Contact Number
                val phoneNumber = "+91 98765 43210"
                Row(
                    horizontalArrangement = Arrangement.spacedBy(pxToDp(12)),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AppCircularIcon(
                        painter = painterResource(R.drawable.ic_call),
                        iconDescription = "Contact",
                        boxSize = pxToDp(36),
                        iconSize = pxToDp(18),
                        iconPadding = pxToDp(6),
                        boxColor = circularBoxColor,
                        tint = primaryColor,
                        onClick = {
                            // Copy phone to clipboard
                            val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Phone", phoneNumber)
                            clipboard.setPrimaryClip(clip)
                            
                            // Open dialer app with number on keypad
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:$phoneNumber")
                            }
                            context.startActivity(intent)
                        }
                    )
                    CustomLabel(
                        header = phoneNumber,
                        fontSize = 14.sp,
                        headerColor = onSurfaceColor.copy(0.8f),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Add Identity Card Option
                Row(
                    horizontalArrangement = Arrangement.spacedBy(pxToDp(12)),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAddIdentityCard() }
                        .padding(vertical = pxToDp(4))
                ) {
                    AppCircularIcon(
                        painter = painterResource(R.drawable.ic_add_image),
                        iconDescription = "Add Identity Card",
                        boxSize = pxToDp(36),
                        iconSize = pxToDp(18),
                        iconPadding = pxToDp(6),
                        boxColor = primaryColor.copy(alpha = 0.1f),
                        tint = primaryColor,
                        onClick = onAddIdentityCard
                    )
                    CustomLabel(
                        header = "Add Identity Card",
                        fontSize = 14.sp,
                        headerColor = primaryColor,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

// Booking History Section with stats cards
@Composable
fun BookingHistorySection() {
    Column(
        verticalArrangement = Arrangement.spacedBy(pxToDp(16))
    ) {
        CustomLabel(
            header = "Booking History",
            fontSize = 18.sp,
            headerColor = onSurfaceColor,
            modifier = Modifier.padding(bottom = pxToDp(4))
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(pxToDp(12))
        ) {
            // Booking Requests Card
            BookingHistoryStatCard(
                title = "Booking Requests",
                value = "12",
                icon = R.drawable.ic_booking_pending,
                modifier = Modifier.weight(1f)
            )

            // Equipment Used Card
            BookingHistoryStatCard(
                title = "Equipment Used",
                value = "8",
                icon = R.drawable.ic_user,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(pxToDp(12))
        ) {
            // Issues Raised Card
            BookingHistoryStatCard(
                title = "Issues Raised",
                value = "3",
                icon = R.drawable.ic_ticket_thread,
                modifier = Modifier.weight(1f)
            )

            // Past Equipment Usage Card
            BookingHistoryStatCard(
                title = "Past Usage",
                value = "24",
                icon = R.drawable.ic_assigned_time,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun BookingHistoryStatCard(
    title: String,
    value: String,
    icon: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = onSurfaceVariant
        ),
        shape = RectangleShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(pxToDp(16)),
            verticalArrangement = Arrangement.spacedBy(pxToDp(12))
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .background(
                        color = primaryColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(pxToDp(8))
                    )
                    .padding(pxToDp(8))
            ) {
                AppCategoryIcon(
                    painter = painterResource(icon),
                    iconDescription = title,
                    iconSize = pxToDp(24),
                    tint = primaryColor
                )
            }

            // Value
            CustomLabel(
                header = value,
                fontSize = 24.sp,
                headerColor = onSurfaceColor,
                modifier = Modifier.padding(top = pxToDp(4))
            )

            // Title
            CustomLabel(
                header = title,
                fontSize = 12.sp,
                headerColor = cardColor
            )
        }
    }
}