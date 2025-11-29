package com.example.rmsjims.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.rmsjims.R
import com.example.rmsjims.data.model.BookingDates
import com.example.rmsjims.data.model.BookingItem
import com.example.rmsjims.data.model.BookingPriority
import com.example.rmsjims.data.model.BookingStatus
import com.example.rmsjims.data.model.BookingTab
import com.example.rmsjims.data.model.InChargeInfo
import com.example.rmsjims.data.model.ProductInfo
import com.example.rmsjims.data.model.Status
import com.example.rmsjims.data.model.TabItem


class BookingScreenViewmodel : ViewModel() {

    private val _tabs = mutableStateListOf(
        TabItem(BookingTab.Booking_Requests, "Booking Requests", R.drawable.ic_booking_pending, true),
        TabItem(BookingTab.Verified_Bookings, "Approved Bookings", R.drawable.ic_booking_verified, false),
        TabItem(BookingTab.Canceled_Bookings, "Rejected Bookings", R.drawable.ic_booking_canceled, false)
    )
    val tabs: List<TabItem> = _tabs

    var selectedTab by mutableStateOf(BookingTab.Booking_Requests)
        private set

    var selectedBookingDates by mutableStateOf<BookingDates?>(null)
        private set

    fun updateSelectedBookingDates(dates: BookingDates) {
        selectedBookingDates = dates
    }

    // Sample bookings data
    private val allBookings = listOf(
        BookingItem(
            id = "1",
            productInfo = ProductInfo(
                R.drawable.temp,
                "Canon EOS R50 V", "IDC", "Photo Studio", Status.PENDING
            ),
            inChargeInfo = InChargeInfo(
                profName = "Sumant Rao",
                asstName = "Akash Kumar Swami",
                asstIcons = listOf(R.drawable.ic_mail, R.drawable.ic_call)
            ),
            bookingDates = BookingDates("2025-08-01", "2025-08-10"),
            bookingStatus = BookingStatus.VERIFICATION_PENDING,
            priority = BookingPriority.HIGH
        ),
        BookingItem(
            id = "2",
            productInfo = ProductInfo(
                R.drawable.temp,
                "Sony Alpha A7III", "LAB-A", "Electronics Lab", Status.BOOKED
            ),
            inChargeInfo = InChargeInfo(
                profName = "Dr. Smith",
                asstName = "Jane Doe",
                asstIcons = listOf(R.drawable.ic_mail, R.drawable.ic_call)
            ),
            bookingDates = BookingDates("2025-07-15", "2025-08-15"),
            bookingStatus = BookingStatus.APPROVED,
            priority = BookingPriority.MEDIUM
        ),
        BookingItem(
            id = "3",
            productInfo = ProductInfo(
                R.drawable.temp,
                "Nikon D850", "LAB-B", "Photography Lab", Status.CANCELLED
            ),
            inChargeInfo = InChargeInfo(
                profName = "Prof. Johnson",
                asstName = "Mike Wilson",
                asstIcons = listOf(R.drawable.ic_mail, R.drawable.ic_call)
            ),
            bookingDates = BookingDates("2025-06-01", "2025-06-10"),
            bookingStatus = BookingStatus.REJECTED,
            priority = BookingPriority.LOW,
            rejectionReason = "Equipment already booked for the requested dates"
        ),
        BookingItem(
            id = "4",
            productInfo = ProductInfo(
                R.drawable.temp,
                "Canon EOS 5D Mark IV", "IDC", "Photo Studio", Status.PENDING
            ),
            inChargeInfo = InChargeInfo(
                profName = "Sumant Rao",
                asstName = "Akash Kumar Swami",
                asstIcons = listOf(R.drawable.ic_mail, R.drawable.ic_call)
            ),
            bookingDates = BookingDates("2025-09-01", "2025-09-05"),
            bookingStatus = BookingStatus.VERIFICATION_PENDING,
            priority = BookingPriority.MEDIUM
        ),
        BookingItem(
            id = "5",
            productInfo = ProductInfo(
                R.drawable.temp,
                "Fujifilm X-T4", "LAB-C", "Media Lab", Status.BOOKED
            ),
            inChargeInfo = InChargeInfo(
                profName = "Dr. Anderson",
                asstName = "Sarah Lee",
                asstIcons = listOf(R.drawable.ic_mail, R.drawable.ic_call)
            ),
            bookingDates = BookingDates("2025-08-20", "2025-09-20"),
            bookingStatus = BookingStatus.APPROVED,
            priority = BookingPriority.HIGH
        )
    )

    val filteredBookings: List<BookingItem>
        get() {
            val bookings = when (selectedTab) {
                BookingTab.Booking_Requests -> allBookings.filter { it.bookingStatus == BookingStatus.VERIFICATION_PENDING }
                BookingTab.Verified_Bookings -> allBookings.filter { it.bookingStatus == BookingStatus.APPROVED }
                BookingTab.Canceled_Bookings -> allBookings.filter { it.bookingStatus == BookingStatus.REJECTED }
            }
            // If we have selected booking dates and we're on Booking Requests tab, update the first booking's dates
            return if (selectedBookingDates != null && selectedTab == BookingTab.Booking_Requests && bookings.isNotEmpty()) {
                bookings.mapIndexed { index, booking ->
                    if (index == 0) {
                        booking.copy(bookingDates = selectedBookingDates!!)
                    } else {
                        booking
                    }
                }
            } else {
                bookings
            }
        }

    fun onTabSelect(tab: BookingTab) {
        selectedTab = tab
        _tabs.replaceAll { it.copy(isSelected = it.tab == tab) }
    }
}
