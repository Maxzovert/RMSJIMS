package com.example.rmsjims.ui.screens.staff

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rmsjims.R
import com.example.rmsjims.data.model.ProductInfo
import com.example.rmsjims.navigation.Screen
import com.example.rmsjims.ui.components.AppButton
import com.example.rmsjims.ui.components.CustomLabel
import com.example.rmsjims.ui.components.CustomTopBar
import com.example.rmsjims.ui.theme.onSurfaceVariant
import com.example.rmsjims.ui.theme.onSurfaceColor
import com.example.rmsjims.ui.theme.daysColor
import com.example.rmsjims.ui.theme.primaryColor
import com.example.rmsjims.ui.theme.errorColor
import com.example.rmsjims.ui.theme.whiteColor
import com.example.rmsjims.util.pxToDp
import com.example.rmsjims.util.ResponsiveLayout
import com.example.rmsjims.viewmodel.BookingScreenViewmodel
import com.example.rmsjims.viewmodel.CalendarViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    navController: NavHostController,
    viewModel: CalendarViewModel = koinViewModel(),
    bookingViewmodel: BookingScreenViewmodel
) {
    val selectedDate = viewModel.selectedDate
    val currentMonth = viewModel.currentMonth
    val dates = viewModel.getMonthDates()
    val months = viewModel.getMonths().take(3)

    val daysOfWeek = remember {
        listOf(
            DayOfWeek.SUNDAY,
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY
        )
    }
    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Booking Dates",
                onNavigationClick = {
                    navController.popBackStack()
                }
            )
        },
        bottomBar = {
            val hasCompleteRange = viewModel.hasCompleteRange()
            if (hasCompleteRange) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .padding(ResponsiveLayout.getResponsivePadding(16.dp, 20.dp, 24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    AppButton(
                        buttonText = "CONFIRM",
                        onClick = {
                            viewModel.confirmSelection()?.let { bookingDates ->
                                bookingViewmodel.updateSelectedBookingDates(bookingDates)
                                navController.navigate(Screen.BookingsScreen.route)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding( 
                    start = ResponsiveLayout.getHorizontalPadding(),
                    end = ResponsiveLayout.getHorizontalPadding(),
                    top = paddingValues.calculateTopPadding(),
                    bottom = 0.dp
                ),
            verticalArrangement = Arrangement.spacedBy(ResponsiveLayout.getResponsivePadding(20.dp, 24.dp, 28.dp))
           ) {

            Spacer(modifier = Modifier.height(ResponsiveLayout.getResponsivePadding(6.dp, 8.dp, 10.dp)))

            MonthTabRow(
                months = months,
                currentMonth = currentMonth,
                onMonthSelected = { viewModel.setMonth(it) }
            )
            DaysOfWeekHeader(daysOfWeek = daysOfWeek)

            // Calendar Grid
            CalendarGrid(
                dates = dates,
                viewModel = viewModel,
                today = LocalDate.now(),
                onDateClick = viewModel::selectDate,
                currentMonth = currentMonth,
            )

            // Status summary card
            StatusCard(
                holidayCount = viewModel.getHolidayCount(),
                bookedDate = viewModel.bookedDate,
                availableDate = viewModel.availableDate,
                bookOnDate = viewModel.bookOnDate,
                today = LocalDate.now()
            )

            //EquipCard
//            EquipBookingCard(
////                productInfo = bookingViewmodel.productInfo,
//            )

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthTabRow(
    months: List<YearMonth>,
    currentMonth: YearMonth,
    onMonthSelected: (YearMonth) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(ResponsiveLayout.getResponsivePadding(12.dp, 16.dp, 20.dp))
    ) {
        items(months) { month ->
            val isSelected = month == currentMonth

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(ResponsiveLayout.getResponsivePadding(4.dp, 6.dp, 8.dp)))
                    .background(if (isSelected) primaryColor else onSurfaceVariant)
                    .clickable { onMonthSelected(month) }
            ) {
                CustomLabel(
                    header = month.month.name.lowercase().replaceFirstChar { it.uppercase() },
                    headerColor = if (isSelected) whiteColor.copy(alpha = 0.9f) else onSurfaceColor.copy(alpha = 0.9f),
                    modifier = Modifier
                        .padding(horizontal = pxToDp(45), vertical = pxToDp(12)),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DaysOfWeekHeader(
    daysOfWeek: List<DayOfWeek>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        daysOfWeek.forEach { day ->
            val isWeekend = day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY
            Box(
                modifier = Modifier
                    .weight(1f) ,// <-- same as CalendarGrid columns
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day.name.first().toString(),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.font_alliance_regular_two)),
                    textAlign = TextAlign.Center,
                    color = if (isWeekend) errorColor else onSurfaceColor.copy(0.9f)
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarGrid(
    dates: List<LocalDate>, // Should be ordered from Sunday to Saturday in each week
    viewModel: CalendarViewModel,
    today: LocalDate,
    currentMonth: YearMonth,
    onDateClick: (LocalDate) -> Unit
) {
    val weeks = dates.chunked(7)
    val selectedStartDate = viewModel.selectedStartDate
    val selectedEndDate = viewModel.selectedEndDate

    Column(
        verticalArrangement = Arrangement.spacedBy(pxToDp(4))
    ) {
        weeks.forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(pxToDp(4))
            ) {
                week.forEach { date ->
                    val isToday = date == today
                    val inMonth = date.month == currentMonth.month
                    val isHoliday = viewModel.isDateHoliday(date)
                    val isBooked = viewModel.isDateBooked(date)
                    val isPast = viewModel.isDateInPast(date)
                    val isInRange = viewModel.isDateInRange(date)
                    val isRangeStart = viewModel.isDateRangeStart(date)
                    val isRangeEnd = viewModel.isDateRangeEnd(date)
                    val isSelected = isRangeStart || isRangeEnd

                    // Determine background color based on legend
                    val backgroundColor = when {
                        isSelected -> primaryColor // Selected (Book On) - primaryColor background
                        isInRange -> primaryColor.copy(alpha = 0.3f) // In range - lighter primary
                        isBooked -> onSurfaceVariant // Booked - onSurfaceVariant background
                        isHoliday -> onSurfaceVariant // Holidays - onSurfaceVariant background
                        isPast -> onSurfaceVariant.copy(alpha = 0.5f) // Past dates - greyed out
                        inMonth -> onSurfaceVariant // Available - onSurfaceVariant background
                        else -> onSurfaceVariant // Out of month
                    }

                    // Determine text color based on legend
                    val textColor = when {
                        isSelected -> whiteColor // Selected - white text
                        isInRange -> primaryColor // In range - primaryColor text
                        isBooked -> errorColor // Booked - errorColor text
                        isHoliday -> errorColor // Holidays - errorColor text
                        isPast -> onSurfaceColor.copy(alpha = 0.4f) // Past dates - greyed out text
                        inMonth -> onSurfaceColor // Available - onSurfaceColor text
                        else -> daysColor // Out of month
                    }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .width(pxToDp(48))
                            .height(pxToDp(60))
                            .clip(RoundedCornerShape(pxToDp(4)))
                            .background(backgroundColor)
                            .border(
                                width = when {
                                    isToday -> pxToDp(1)
                                    else -> 0.dp
                                },
                                color = when {
                                    isToday -> primaryColor
                                    else -> Color.Transparent
                                },
                                shape = RoundedCornerShape(pxToDp(4))
                            )
                            .then(
                                if (isPast) {
                                    Modifier // Past dates are not clickable
                                } else {
                                    Modifier.clickable { onDateClick(date) }
                                }
                            )
                    ) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            color = textColor,
                            style = MaterialTheme.typography.bodySmall.copy(
                                textDecoration = if (isBooked) TextDecoration.LineThrough else TextDecoration.None
                            )
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StatusCard(
    holidayCount: Int,
    bookedDate: LocalDate,
    availableDate: LocalDate,
    bookOnDate: LocalDate,
    today: LocalDate,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults
            .cardColors(containerColor = onSurfaceVariant),
        modifier = modifier.fillMaxWidth(),
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatusLabel(
                label = "Holidays",
                value = holidayCount.toString(),
                valueColor = errorColor
            )
            StatusLabel(
                label = "Booked",
                value = bookedDate.dayOfMonth.toString(),
                valueColor = errorColor,
                isStrikethrough = true
            )
            StatusLabel(
                label = "Today",
                value = today.dayOfMonth.toString(),
                valueColor = primaryColor
            )
            StatusLabel(
                label = "Available",
                value = availableDate.dayOfMonth.toString(),
                valueColor = onSurfaceColor
            )
            StatusLabel(
                label = "Book On",
                value = bookOnDate.dayOfMonth.toString(),
                valueColor = whiteColor,
                containerColor = primaryColor,
                isBoxed = true
            )
        }
    }
}


@Composable
fun StatusLabel(
    label: String,
    value: String,
    valueColor: Color,
    containerColor: Color = primaryColor,
    isStrikethrough: Boolean = false,
    isBoxed: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(IntrinsicSize.Max)
    ) {
        Box(
            modifier = if (isBoxed) {
                Modifier
                    .background(containerColor)
                    .padding(horizontal = pxToDp(10), vertical = pxToDp(2))
            } else Modifier
        ) {
            Text(
                text = value,
                color = valueColor,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(
                    textDecoration = if (isStrikethrough) TextDecoration.LineThrough else TextDecoration.None
                )
            )
        }
        Spacer(modifier = Modifier.height(pxToDp(18)))
        Text(
            text = label,
            fontFamily = FontFamily(Font(R.font.font_alliance_regular_two)),
            textAlign = TextAlign.Center,
            color = onSurfaceColor,
            fontSize = 14.sp
        )
    }
}

@Composable
fun EquipBookingCard(
    productInfo: ProductInfo,
    cardShape: Shape = RectangleShape,
    containerColor : Color = whiteColor,
    cardPadding : Dp = pxToDp(20)
) {
    Card(
        shape = cardShape,
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {
        Column(modifier = Modifier.padding(cardPadding)) {

            Row {
                Image(
                    painter = painterResource(R.drawable.temp),
                    contentDescription = "Product Image",

                    modifier = Modifier
                        .size(pxToDp(76))
                )
                Spacer(modifier = Modifier.width(pxToDp(30)))
                Column(
                    modifier = Modifier.weight(0.8f),
                    verticalArrangement = Arrangement.spacedBy(pxToDp(9))
                ) {
                    EquipBookingItem("Item", "Canon EOS R50 V")
                    EquipBookingItem("Location", "IDC School of Design")
                    EquipBookingItem("Timing", "(09:00am - 05:30pm)", primaryColor)
                }
            }
        }
    }
}

@Composable
fun EquipBookingItem(
    label : String,
    value : String,
    headerColor: Color = onSurfaceColor
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
       CustomLabel(
           header = label,
           headerColor = onSurfaceColor.copy(0.5f),
           fontSize = 14.sp
       )
       CustomLabel(
           header = value,
           headerColor = headerColor,
           fontSize = 14.sp
       )
    }
}


//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//@Preview(showBackground = true)
//fun CalendarScreenPreview() {
//    CalendarScreen(viewModel = CalendarViewModel())
//}
//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true)
//@Composable
//fun MonthTabRowPreview() {
//    val months = remember { (0..5).map { YearMonth.now().plusMonths(it.toLong()) } }
//    var selectedMonth by remember { mutableStateOf(YearMonth.now()) }
//
//    MonthTabRow(
//        months = months.take(3),
//        currentMonth = selectedMonth,
//        onMonthSelected = { selectedMonth = it }
//    )
//}

//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true, name = "Days Of Week Header")
//@Composable
//fun DaysOfWeekHeaderPreview() {
//    val daysOfWeek = listOf(
//        DayOfWeek.SUNDAY,
//        DayOfWeek.MONDAY,
//        DayOfWeek.TUESDAY,
//        DayOfWeek.WEDNESDAY,
//        DayOfWeek.THURSDAY,
//        DayOfWeek.FRIDAY,
//        DayOfWeek.SATURDAY
//    )
//
//    MaterialTheme {
//        Surface(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
//            DaysOfWeekHeader(
//                daysOfWeek = daysOfWeek
//            )
//        }
//    }
//}


//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//@Preview(showBackground = true)
//fun CalendarGridPreview() {
//    val sampleDates = generateSequence(LocalDate.now().withDayOfMonth(1)) { it.plusDays(1) }
//        .take(42) // 6 weeks
//        .toList()
//
//    CalendarGrid(
//        dates = sampleDates,
//        selectedDate = LocalDate.now().plusDays(3),
//        today = LocalDate.now(),
//        onDateClick = {}
//    )
//}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun StatusCardPreview() {
    StatusCard(
        holidayCount = 4,
        bookedDate = LocalDate.now().minusDays(2),
        today = LocalDate.now(),
        availableDate = LocalDate.now().plusDays(3),
        bookOnDate = LocalDate.now().plusDays(5)
    )
}

//@Composable
//@Preview(showBackground = true)
//fun StatusLabelPreview() {
//    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
//        StatusLabel("Booked", "2025-08-01", isStrikethrough = true)
//        StatusLabel("Today", "2025-07-28")
//    }
//}
