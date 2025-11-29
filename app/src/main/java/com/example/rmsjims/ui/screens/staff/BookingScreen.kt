package com.example.rmsjims.ui.screens.staff

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import com.example.rmsjims.R
import com.example.rmsjims.data.model.BookingDates
import com.example.rmsjims.data.model.BookingItem
import com.example.rmsjims.data.model.BookingStatus
import com.example.rmsjims.data.model.BookingTab
import com.example.rmsjims.data.model.InChargeInfo
import com.example.rmsjims.data.model.ProductInfo
import com.example.rmsjims.data.model.TabItem
import com.example.rmsjims.navigation.Screen
import com.example.rmsjims.ui.components.AppButton
import com.example.rmsjims.ui.components.AppNavIcon
import com.example.rmsjims.ui.components.CustomLabel
import com.example.rmsjims.ui.components.CustomNavigationBar
import com.example.rmsjims.ui.components.CustomTopBar
import com.example.rmsjims.ui.components.EditButton
import com.example.rmsjims.ui.theme.onSurfaceVariant
import com.example.rmsjims.ui.theme.categoryIconColor
import com.example.rmsjims.ui.theme.onSurfaceColor
import com.example.rmsjims.ui.theme.primaryColor
import com.example.rmsjims.ui.theme.cardColor
import com.example.rmsjims.ui.theme.whiteColor
import com.example.rmsjims.util.pxToDp
import com.example.rmsjims.util.ResponsiveLayout
import com.example.rmsjims.viewmodel.BookingScreenViewmodel
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun BookingScreen(
    navController: NavHostController,
    viewModel: BookingScreenViewmodel = koinViewModel()
) {
    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Bookings",
                navController = navController
            )
        },
        bottomBar = {
            CustomNavigationBar(navController = navController)
        },
    ) { paddingValues ->

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(ResponsiveLayout.getHorizontalPadding())
            .padding(paddingValues)) {
            Spacer(modifier = Modifier.height(ResponsiveLayout.getResponsivePadding(27.dp, 32.dp, 38.dp)))

            TabSelector(
                tabs = viewModel.tabs,
                onTabSelected = { selectedTab ->
                    viewModel.onTabSelect(selectedTab)
                }
            )

            Spacer(modifier = Modifier.height(ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp)))

            // Observe selectedTab to ensure recomposition when it changes
            val selectedTab = viewModel.selectedTab
            val filteredBookings = remember(selectedTab) {
                viewModel.filteredBookings
            }
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp))
            ) {
                items(filteredBookings) { booking ->
                    InfoCard(
                        bookingItem = booking,
                        onEditBooking = { navController.navigate(Screen.CalendarScreen.route) },
                        onExtendBooking = { /* Handle extend booking */ },
                        onReRequestBooking = { /* Handle re-request booking */ }
                    )
                }
            }
        }
    }
}


//endregion
@Composable
fun TabSelector(tabs: List<TabItem>, onTabSelected: (BookingTab) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        tabs.forEach {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp) // apply padding here, outside the clickable column
                    .clickable { onTabSelected(it.tab) }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(pxToDp(10))
                ) {
                    AppNavIcon(
                        painter = painterResource(id = it.iconRes),
                        iconDescription = it.label,
                        iconSize = pxToDp(20),
                        tint = if (it.isSelected) primaryColor else categoryIconColor
                    )
                    CustomLabel(
                        header = it.label,
                        fontSize = 12.sp,
                        headerColor = if (it.isSelected) primaryColor else categoryIconColor
                    )
                }
            }

        }
    }
}

@Composable
fun InfoCard(
    bookingItem: BookingItem,
    onEditBooking: () -> Unit,
    onExtendBooking: () -> Unit = {},
    onReRequestBooking: () -> Unit = {},
    icons: List<Int> = listOf(R.drawable.ic_mail),
    cardShape: Shape = RectangleShape,
    containerColor : Color = onSurfaceVariant,
) {
    val bookingStatus = bookingItem.bookingStatus
    val productInfo = bookingItem.productInfo
    val inChargeInfo = bookingItem.inChargeInfo
    val bookingDates = bookingItem.bookingDates
    val priority = bookingItem.priority
    
    Card(
        shape = cardShape,
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = pxToDp(20), vertical = pxToDp(22)),
            verticalArrangement = Arrangement.spacedBy(pxToDp(20))
        ) {

            Row {
                Image(
                    painter = painterResource(productInfo.imageRes),
                    contentDescription = "Product Image",
                    modifier = Modifier.size(pxToDp(76))
                )
                Spacer(modifier = Modifier.width(pxToDp(30)))
                Column(
                    modifier = Modifier.weight(0.8f),
                    verticalArrangement = Arrangement.spacedBy(pxToDp(9))
                ) {
                    CustomLabel(
                        header = productInfo.title,
                        fontSize = 16.sp,
                        modifier = Modifier,
                        headerColor = onSurfaceColor
                    )
                    CustomLabel(
                        header = productInfo.location,
                        fontSize = 12.sp,
                        modifier = Modifier,
                        headerColor = cardColor
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(pxToDp(8)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Status Tag
                        CustomLabel(
                            header = bookingStatus.label,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .background(
                                    color = bookingStatus.color,
                                    shape = RoundedCornerShape(pxToDp(20))
                                )
                                .padding(horizontal = pxToDp(10), vertical = pxToDp(4)),
                            headerColor = whiteColor
                        )
                        // Priority Tag
                        CustomLabel(
                            header = priority.label,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .background(
                                    color = priority.color,
                                    shape = RoundedCornerShape(pxToDp(20))
                                )
                                .padding(horizontal = pxToDp(10), vertical = pxToDp(4)),
                            headerColor = whiteColor
                        )
                    }
                }
            }

            Divider(thickness = pxToDp(1), color = cardColor)

            Column(verticalArrangement = Arrangement.spacedBy(pxToDp(16))) {
                CustomLabel(
                    header = "InCharge",
                    headerColor = onSurfaceColor.copy(0.9f)
                )
                InChargeRow(label = "Prof.", name = inChargeInfo.profName)
                InChargeRow(
                    label = "Asst.",
                    name = inChargeInfo.asstName,
                    icons = inChargeInfo.asstIcons,
                    email = null, // Email not available in InChargeInfo
                    phone = null  // Phone not available in InChargeInfo
                )
            }

            Divider(thickness = pxToDp(1), color = cardColor)
            
            Column(verticalArrangement = Arrangement.spacedBy(pxToDp(18))) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = pxToDp(2)),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CustomLabel(
                        header = "Booking Dates",
                        headerColor = onSurfaceColor.copy(0.9f)
                    )
                    // Show Edit button only for Booking Request tab
                    if (bookingStatus == BookingStatus.VERIFICATION_PENDING) {
                        EditButton(
                            onClick = onEditBooking
                        )
                    }
                }
                DatesRow(label = "From", name = bookingDates.fromDate)
                DatesRow(label = "To", name = bookingDates.toDate)
            }

            // Timeline progress bar for Approved Bookings
            if (bookingStatus == BookingStatus.APPROVED) {
                Divider(thickness = pxToDp(1), color = cardColor)
                BookingProgressBar(
                    fromDate = bookingDates.fromDate,
                    toDate = bookingDates.toDate
                )
                Spacer(modifier = Modifier.height(pxToDp(8)))
                AppButton(
                    buttonText = "Extend Booking",
                    onClick = onExtendBooking,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Rejection reason and Re-request button for Rejected Bookings
            if (bookingStatus == BookingStatus.REJECTED) {
                bookingItem.rejectionReason?.let { reason ->
                    Divider(thickness = pxToDp(1), color = cardColor)
                    Column(verticalArrangement = Arrangement.spacedBy(pxToDp(12))) {
                        CustomLabel(
                            header = "Rejection Reason",
                            headerColor = onSurfaceColor.copy(0.9f),
                            fontSize = 14.sp
                        )
                        CustomLabel(
                            header = reason,
                            headerColor = onSurfaceColor.copy(0.7f),
                            fontSize = 13.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(pxToDp(8)))
                    AppButton(
                        buttonText = "Re-request Booking",
                        onClick = onReRequestBooking,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

//@Composable
//fun InChargeRow(label: String, name: String, icons: List<Int>? = null) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween,
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            CustomLabel(
//                header = "$label",
//                fontSize = 14.sp,
//                headerColor = onSurfaceColor.copy(0.5f),
//                modifier = Modifier.width(50.dp)
//            )
//            CustomLabel(
//                header = name,
//                fontSize = 14.sp,
//                modifier = Modifier,
//                headerColor = onSurfaceColor.copy(0.8f)
//
//            )
//        }
//        icons?.let {
//            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//                it.forEach { iconRes ->
//                    Icon(
//                        painter = painterResource(id = iconRes),
//                        contentDescription = "Contact Icon"
//                    )
//                }
//            }
//        }
//    }
//}


@Composable
fun DatesRow(label: String, name: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomLabel(
                header = "$label",
                fontSize = 16.sp,
                headerColor = onSurfaceColor.copy(0.5f),
                modifier = Modifier
                    .width(80.dp)
            )
            CustomLabel(
                header = name,
                fontSize = 16.sp,
                modifier = Modifier,
                headerColor = onSurfaceColor.copy(0.8f)

            )
        }
    }
}

@Composable
fun BookingProgressBar(fromDate: String, toDate: String) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val startDate = dateFormat.parse(fromDate)
    val endDate = dateFormat.parse(toDate)
    val today = Date()

    // If parsing fails, show fallback UI
    if (startDate == null || endDate == null) {
        CustomLabel(
            header = "Invalid date format",
            headerColor = cardColor,
            fontSize = 12.sp
        )
        return
    }

    val totalDuration = endDate.time - startDate.time
    val elapsedDuration = today.time - startDate.time

    val progress = when {
        today.before(startDate) -> 0f
        today.after(endDate) -> 1f
        else -> (elapsedDuration.toFloat() / totalDuration.toFloat()).coerceIn(0f, 1f)
    }

    Column(verticalArrangement = Arrangement.spacedBy(pxToDp(8))) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CustomLabel(
                header = "Booking Progress",
                headerColor = onSurfaceColor.copy(0.9f),
                fontSize = 14.sp
            )
            CustomLabel(
                header = "${(progress * 100).toInt()}%",
                headerColor = primaryColor,
                fontSize = 14.sp
            )
        }

        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(pxToDp(8)),
            color = primaryColor,
            trackColor = cardColor.copy(alpha = 0.3f)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CustomLabel(
                header = fromDate,
                headerColor = cardColor,
                fontSize = 12.sp
            )
            CustomLabel(
                header = toDate,
                headerColor = cardColor,
                fontSize = 12.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBookingScreen() {
//    BookingScreen()
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewBookingTabSelector() {
//    BookingTabSelector(
//        tabs = listOf(
//            TabItem(BookingTab.Booking_Requests,  "Booking Requests",R.drawable.ic_booking_pending, isSelected = true),
//            TabItem(BookingTab.Booking_Requests, "Booking Requests",R.drawable.ic_booking_pending, isSelected = false),
//            TabItem(BookingTab.Booking_Requests, "Booking Requests",R.drawable.ic_booking_pending, isSelected = false)
//        ),
//        onTabSelected = {}
//    )
//}

@Preview(showBackground = true)
@Composable
fun PreviewInfoCard() {
//
}

